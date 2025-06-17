package com.thitsaworks.operation_portal.api.participant.security;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.component.common.type.PrincipalStatus;
import com.thitsaworks.component.common.type.RealmType;
import com.thitsaworks.operation_portal.api.participant.security.exception.AccountInactiveException;
import com.thitsaworks.operation_portal.api.participant.security.exception.AuthenticationFailureException;
import com.thitsaworks.operation_portal.api.participant.security.exception.InvalidAccessKeyException;
import com.thitsaworks.operation_portal.component.http.CachedBodyHttpServletRequest;
import com.thitsaworks.operation_portal.component.security.DfspCrypto;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class ApiAuthenticator implements Authenticator {

    private final static Logger LOG = LoggerFactory.getLogger(ApiAuthenticator.class);

    private final PrincipalCache principalCache;

    private final ParticipantUserCache participantUserCache;


    @Override
    public UserContext authenticate(CachedBodyHttpServletRequest cachedBodyRequest)
            throws InvalidAccessKeyException, AccountInactiveException, AuthenticationFailureException {

        // Rules :

        // 1. Request's submitted time must be within 15 minutes when it reached
        // the server
        // 2. Any invalid data in request, then reject the request
        // 3. Signatures must be matched.

        Long accessKey = 0L;

        try {

            accessKey = Long.parseLong(cachedBodyRequest.getHeader("X-ACCESS-KEY"));

        } catch (Exception e) {

            throw new InvalidAccessKeyException(accessKey.toString());
        }

        PrincipalData principalData = this.principalCache.get(new AccessKey(accessKey), RealmType.PARTICIPANT);

        if (principalData == null) {

            LOG.error("PrincipalData cannot be found for accessKey : [{}]", accessKey);

            throw new InvalidAccessKeyException(accessKey.toString());

        }

        ParticipantUserData participantUserData =
                this.participantUserCache.get(new ParticipantUserId(principalData.getPrincipalId()
                                                                                 .getId()));

        if (participantUserData.status() != PrincipalStatus.ACTIVE) {

            LOG.warn("Account is denied for accessKey : [{}]", accessKey);

            throw new AccountInactiveException(accessKey.toString());
        }

        // Calculate first part
        String method = cachedBodyRequest.getMethod();
        String uri = cachedBodyRequest.getRequestURI();
        String submittedAuthHeader = cachedBodyRequest.getHeader("X-AUTH-HEADER");
        LOG.info("X-AUTH-HEADER : [{}]", submittedAuthHeader);

        String payload = new String(cachedBodyRequest.getCachedBody(), StandardCharsets.UTF_8);
        payload = payload == null || payload.isEmpty() ? "<BLANK>" : payload;
        LOG.info("payload : [{}]", payload);

        String signatureOfPayload = DigestUtils.sha256Hex(payload).toUpperCase();

        LOG.info("signatureOfPayload : [{}]", signatureOfPayload);

        String checking = method + "|" + uri + "|" + signatureOfPayload;
        LOG.info("checking : [{}]", checking);

        String secret = principalData.getSecretKey();

        LOG.info("secret : [{}]", secret);

        String calculatedAuthHeader = BaseEncoding.base16()
                                                  .encode(DfspCrypto.hmacSha256(secret.getBytes(Charsets.UTF_8),
                                                          checking.getBytes(Charsets.UTF_8)));

        if (calculatedAuthHeader == null) {

            throw new AuthenticationFailureException();

        }

        LOG.info("calculatedAuthHeader : [{}]", calculatedAuthHeader);

        if (submittedAuthHeader.equalsIgnoreCase(calculatedAuthHeader)) {

            LOG.info("Auth header validation is successful.");

            return new UserContext(new ParticipantUserId(principalData.getPrincipalId().getId()), new AccessKey(accessKey));

        }

        throw new AuthenticationFailureException();

    }

}

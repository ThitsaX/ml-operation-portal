package com.thitsaworks.operation_portal.api.participant.security;

import com.thitsaworks.operation_portal.api.participant.security.exception.AccountInactiveException;
import com.thitsaworks.operation_portal.api.participant.security.exception.AuthenticationFailureException;
import com.thitsaworks.operation_portal.api.participant.security.exception.InvalidAccessKeyException;
import com.thitsaworks.operation_portal.component.http.CachedBodyHttpServletRequest;

import java.io.IOException;

public interface Authenticator {

    UserContext authenticate(CachedBodyHttpServletRequest cachedBodyHttpServletRequest)
            throws IOException, InvalidAccessKeyException, AccountInactiveException, AuthenticationFailureException;

}

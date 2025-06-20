package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditByParticipantAndUserQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.usecase.common.GetAllAudit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllAuditHandler extends GetAllAudit {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAudit.class);

    private final GetAuditByParticipantAndUserQuery getAuditQuery;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    @CoreWriteTransactional
    public Output onExecute(Input input) throws Exception {

        GetAuditByParticipantAndUserQuery.Output output =
                this.getAuditQuery.execute(new GetAuditByParticipantAndUserQuery.Input(input.realmId(),
                                                                                       input.userId(),
                                                                                       input.fromDate(),
                                                                                       input.toDate()));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (GetAuditByParticipantAndUserQuery.Output.AuditInfo data : output.getAuditInfoList()) {

            auditInfoList.add(
                    new Output.AuditInfo(
                            data.getParticipantName(),
                            data.getUserName(),
                            data.getActionName(),
                            data.getInputInfo(),
                            data.getOutputInfo(),
                            data.getActionDate()));
        }

        return new Output(auditInfoList);
    }

    @Override
    protected String getName() {

        return GetAllAudit.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_common";
    }

    @Override
    protected String getId() {

        return GetAllAudit.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        switch (principalData.userRoleType()) {

            case OPERATION:
            case ADMIN:
                return true;
            case SUPERUSER:
            case REPORTING:
                return false;
        }

        return false;
    }

    @Override
    public void onAudit(GetAllAudit.Input input, GetAllAudit.Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetAllAudit.class, input, null,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}

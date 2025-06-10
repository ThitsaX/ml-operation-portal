package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.audit.query.GetAudit;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.usecase.common.GetAllAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllAuditBean extends GetAllAudit {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAudit.class);

    @Autowired
    private GetAudit getAudit;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @WriteTransactional
    public Output onExecute(Input input) throws Exception {

        GetAudit.Output output = this.getAudit.execute(new GetAudit.Input(input.getRealmId(),
                input.getUserId(), input.getFromDate(), input.getToDate()));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (GetAudit.Output.AuditInfo data : output.getAuditInfoList()) {

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
                this.principalCache.get(new AccessKey(Long.parseLong(securityContext.getAccessKey())));

        switch (principalData.getUserRoleType()) {

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
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}

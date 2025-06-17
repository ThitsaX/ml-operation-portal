package com.thitsaworks.operation_portal.usecase.common.impl;

import com.thitsaworks.operation_portal.audit.query.GetAuditByParticipant;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.usecase.common.GetAllAudit;
import com.thitsaworks.operation_portal.usecase.common.GetAllAuditByParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllAuditByParticipantBean extends GetAllAuditByParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAudit.class);

    @Autowired
    private GetAuditByParticipant getAuditByParticipant;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @DfspWriteTransactional
    public Output onExecute(Input input) throws Exception {

        GetAuditByParticipant.Output output = this.getAuditByParticipant.execute(new GetAuditByParticipant.Input(input.getRealmId(),
                input.getFromDate(), input.getToDate()));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (GetAuditByParticipant.Output.AuditInfo data : output.getAuditInfoList()) {

            auditInfoList.add(
                    new Output.AuditInfo(
                            data.getUserName(),
                            data.getActionName(),
                            data.getActionDate()));
        }

        return new Output(auditInfoList);
    }

    @Override
    protected String getName() {

        return GetAllAuditByParticipant.class.getCanonicalName();
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

        return GetAllAuditByParticipant.class.getName();
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


}

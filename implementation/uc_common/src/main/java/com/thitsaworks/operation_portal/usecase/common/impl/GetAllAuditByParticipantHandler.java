package com.thitsaworks.operation_portal.usecase.common.impl;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.usecase.common.GetAllAudit;
import com.thitsaworks.operation_portal.usecase.common.GetAllAuditByParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllAuditByParticipantHandler extends GetAllAuditByParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAudit.class);

    private final GetAllAuditByParticipantQuery getAllAuditByParticipantQuery;

    private final PrincipalCache principalCache;

    @Override
    public Output onExecute(Input input) throws Exception {

        GetAllAuditByParticipantQuery.Output output =
                this.getAllAuditByParticipantQuery.execute(new GetAllAuditByParticipantQuery.Input(
                        input.realmId(),
                        input.fromDate(),
                        input.toDate()));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (GetAllAuditByParticipantQuery.Output.AuditInfo data : output.getAuditInfoList()) {

            auditInfoList.add(new Output.AuditInfo(
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
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        return switch (principalData.userRoleType()) {
            case OPERATION, ADMIN -> true;
            case SUPERUSER, REPORTING -> false;
        };

    }


}

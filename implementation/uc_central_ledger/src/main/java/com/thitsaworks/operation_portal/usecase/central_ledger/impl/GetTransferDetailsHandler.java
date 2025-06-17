package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransferDetail;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetTransferDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTransferDetailsHandler extends GetTransferDetails {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailsHandler.class);

    private GetTransferDetail getTransferDetail;

    private ObjectMapper objectMapper;

    private PrincipalCache principalCache;

    @Autowired
    public GetTransferDetailsHandler(GetTransferDetail getTransferDetail,
                                     ObjectMapper objectMapper,
                                     PrincipalCache principalCache) {

        this.getTransferDetail = getTransferDetail;
        this.objectMapper = objectMapper;
        this.principalCache = principalCache;
    }

    @Override
    public Output onExecute(Input input) throws Exception {

        GetTransferDetail.Output output = this.getTransferDetail.execute(new GetTransferDetail.Input(
                input.getTransferId()));

        return new Output(output.getBusinessData());
    }

    @Override
    protected String getName() {

        return GetTransferDetails.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_central_ledger";
    }

    @Override
    protected String getId() {

        return GetTransferDetails.class.getName();
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

        return switch (principalData.getUserRoleType()) {
            case OPERATION -> true;
            case SUPERUSER, ADMIN, REPORTING -> false;
        };

    }

    @Override
    public void onAudit(Input input, Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetTransferDetails.class, input, output,
                      new UserId(Long.valueOf(securityContext.getUserId())));
    }

}

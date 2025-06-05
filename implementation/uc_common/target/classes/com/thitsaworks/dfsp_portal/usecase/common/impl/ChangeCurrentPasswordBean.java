package com.thitsaworks.dfsp_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.iam.domain.command.ChangePassword;
import com.thitsaworks.dfsp_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.dfsp_portal.iam.query.data.PrincipalData;
import com.thitsaworks.dfsp_portal.usecase.common.ChangeCurrentPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChangeCurrentPasswordBean extends ChangeCurrentPassword {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeCurrentPasswordBean.class);

    @Autowired
    private ChangePassword changePassword;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @WriteTransactional
    public Output onExecute(Input input) throws PasswordAuthenticationFailureException, PrincipalNotFoundException {

        ChangePassword.Output changePasswordOutput = this.changePassword.execute(
                new ChangePassword.Input(input.getPrincipalId(), input.getOldPassword(),
                        input.getNewPassword()));

        return new Output(changePasswordOutput.getAccessKey(),
                changePasswordOutput.getSecretKey());
    }

    @Override
    protected String getName() {

        return ChangeCurrentPassword.class.getCanonicalName();
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

        return ChangeCurrentPassword.class.getName();
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
    public void onAudit(ChangeCurrentPasswordBean.Input input, ChangeCurrentPasswordBean.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ChangeCurrentPassword.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}

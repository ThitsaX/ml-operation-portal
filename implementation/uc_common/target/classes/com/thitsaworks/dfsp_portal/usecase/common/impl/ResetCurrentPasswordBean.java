package com.thitsaworks.dfsp_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.iam.domain.command.ResetPassword;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.dfsp_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.dfsp_portal.iam.query.data.PrincipalData;
import com.thitsaworks.dfsp_portal.participant.exception.EmailNotFoundException;
import com.thitsaworks.dfsp_portal.participant.query.FindUserByEmail;
import com.thitsaworks.dfsp_portal.usecase.common.ResetCurrentPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ResetCurrentPasswordBean extends ResetCurrentPassword {

    private static final Logger LOG = LoggerFactory.getLogger(ResetCurrentPasswordBean.class);

    @Autowired
    private ResetPassword resetPassword;

    @Autowired
    private FindUserByEmail findUserByEmail;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @WriteTransactional
    public Output onExecute(Input input) throws Exception {

        FindUserByEmail.Output findUserByEmailOutput =
                this.findUserByEmail.execute(new FindUserByEmail.Input(input.getEmail()));

        if (findUserByEmailOutput.getUserId().isEmpty()) {

            throw new EmailNotFoundException(input.getEmail().getValue());
        }

        ResetPassword.Output resetPasswordOutput = this.resetPassword.execute(
                new ResetPassword.Input(new PrincipalId(findUserByEmailOutput.getUserId().get().getId()),
                        input.getPassword())
        );

        return new Output(resetPasswordOutput.getAccessKey(),
                          resetPasswordOutput.getSecretKey(),
                          resetPasswordOutput.isUpdated());
    }

    @Override
    protected String getName() {

        return ResetCurrentPassword.class.getCanonicalName();
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

        return ResetCurrentPassword.class.getName();
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
    public void onAudit(ResetCurrentPassword.Input input, ResetCurrentPassword.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ResetCurrentPassword.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}

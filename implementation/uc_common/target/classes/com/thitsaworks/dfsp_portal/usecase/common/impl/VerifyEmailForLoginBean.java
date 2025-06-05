package com.thitsaworks.dfsp_portal.usecase.common.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.usecase.common.VerifyEmailForLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VerifyEmailForLoginBean extends VerifyEmailForLogin {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyEmailForLoginBean.class);

    @Override
    @WriteTransactional
    public Output onExecute(Input input) throws Exception {

        return null;
    }

    @Override
    protected String getName() {

        return VerifyEmailForLogin.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_participant";
    }

    @Override
    protected String getId() {

        return VerifyEmailForLogin.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

}

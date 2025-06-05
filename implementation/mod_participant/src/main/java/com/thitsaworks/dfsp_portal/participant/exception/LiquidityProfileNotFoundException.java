package com.thitsaworks.dfsp_portal.participant.exception;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;

import java.text.MessageFormat;

public class LiquidityProfileNotFoundException extends DFSPPortalException {

    public LiquidityProfileNotFoundException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "LIQUIDITY_PROFILE_NOT_FOUND";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("System cannot find the liquidity profile with ID : ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Liquidity Profile ID.";
    }

}

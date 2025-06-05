package com.thitsaworks.dfsp_portal.api.hub_operator.security;

import com.thitsaworks.dfsp_portal.api.hub_operator.security.exception.AccountInactiveException;
import com.thitsaworks.dfsp_portal.api.hub_operator.security.exception.AuthenticationFailureException;
import com.thitsaworks.dfsp_portal.api.hub_operator.security.exception.InvalidAccessKeyException;
import com.thitsaworks.dfsp_portal.component.http.CachedBodyHttpServletRequest;

import java.io.IOException;

public interface Authenticator {

    UserContext authenticate(CachedBodyHttpServletRequest cachedBodyHttpServletRequest)
            throws IOException, InvalidAccessKeyException, AccountInactiveException, AuthenticationFailureException;

}

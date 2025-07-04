package com.thitsaworks.operation_portal.api.hub_operator.security;

import com.thitsaworks.operation_portal.api.hub_operator.security.exception.AccessDeniedException;
import com.thitsaworks.operation_portal.component.misc.http.CachedBodyHttpServletRequest;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;

import java.io.IOException;

public interface Authenticator {

    SecurityContext authenticate(CachedBodyHttpServletRequest cachedBodyHttpServletRequest)
            throws
            IOException,
            AccessDeniedException;

}

package com.thitsaworks.operation_portal.component.security;

import lombok.Value;

@Value
public class SecurityContext {

    private String userId;

    private String accessKey;

}

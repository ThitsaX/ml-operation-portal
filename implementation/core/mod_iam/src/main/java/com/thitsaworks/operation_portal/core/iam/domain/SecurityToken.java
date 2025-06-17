package com.thitsaworks.operation_portal.core.iam.domain;

import com.thitsaworks.component.common.identifier.AccessKey;
import lombok.Value;

import java.io.Serializable;

@Value
public class SecurityToken implements Serializable {

    private AccessKey accessKey;

    private String secretKey;

}

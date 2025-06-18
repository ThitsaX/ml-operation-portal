package com.thitsaworks.operation_portal.core.iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import lombok.Value;

import java.io.Serializable;

@Value
public class SecurityToken implements Serializable {

    private AccessKey accessKey;

    private String secretKey;

}

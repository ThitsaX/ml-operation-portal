package com.thitsaworks.operation_portal.iam.domain;

import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import lombok.Value;

import java.io.Serializable;

@Value
public class SecurityToken implements Serializable {

    private AccessKey accessKey;

    private String secretKey;

}

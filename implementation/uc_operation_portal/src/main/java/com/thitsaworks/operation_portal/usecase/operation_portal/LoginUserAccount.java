package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.common.type.Email;

public interface LoginUserAccount {

    Output execute(Input input) throws DomainException;

    record Input(Email email, String passwordPlain) { }

    record Output(AccessKey accessKey, String secretKey) { }

}

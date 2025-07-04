package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public interface LoginUserAccount extends
                                  UseCase<LoginUserAccount.Input, LoginUserAccount.Output> {

    record Input(Email email, String passwordPlain) { }

    record Output(AccessKey accessKey, String secretKey) { }

}

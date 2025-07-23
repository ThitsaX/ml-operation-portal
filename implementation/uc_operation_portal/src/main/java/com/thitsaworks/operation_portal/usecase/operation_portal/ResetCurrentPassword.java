package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.common.type.Email;

public interface ResetCurrentPassword extends
                                      UseCase<ResetCurrentPassword.Input, ResetCurrentPassword.Output> {

    record Input(Email email, String password) { }

    record Output(AccessKey accessKey,
                  String secretKey,
                  Boolean updated) { }

}

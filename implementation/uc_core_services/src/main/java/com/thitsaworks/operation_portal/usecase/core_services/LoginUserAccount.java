package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.exception.IAMIgnorableException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface LoginUserAccount {

    Output execute(Input input) throws DomainException;

    record Input(Email email, String passwordPlain) { }

    record Output(AccessKey accessKey, String secretKey) { }

}

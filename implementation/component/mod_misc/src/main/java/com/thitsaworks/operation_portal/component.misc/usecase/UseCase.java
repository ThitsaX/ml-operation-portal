package com.thitsaworks.operation_portal.component.misc.usecase;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

public interface UseCase<I, O> {

    O execute(I input) throws OperationPortalException;

}

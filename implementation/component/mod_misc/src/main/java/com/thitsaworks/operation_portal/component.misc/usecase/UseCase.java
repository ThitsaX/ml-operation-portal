package com.thitsaworks.operation_portal.component.misc.usecase;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;

public interface UseCase<I, O> {

    O execute(I input) throws DomainException;

}

package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.Set;

public interface GetMadeBy extends UseCase<GetMadeBy.Input, GetMadeBy.Output> {

record Input(){}

record Output(Set<User> madeBy){}
    public record User(UserId userId){}
}

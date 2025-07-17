package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.Set;

public interface GetAllAction extends UseCase<GetAllAction.Input,GetAllAction.Output> {

    record Input(){}

    record Output(Set<String> actionNames){}
}

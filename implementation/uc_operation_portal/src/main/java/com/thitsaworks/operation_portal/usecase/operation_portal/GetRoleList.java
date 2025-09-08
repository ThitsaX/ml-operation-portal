package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;

import java.util.List;

public interface GetRoleList extends UseCase<GetRoleList.Input, GetRoleList.Output> {

    record Input() { }

    record Output(List<RoleData> roleList) { }

}

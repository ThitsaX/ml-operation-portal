package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;

import java.util.List;

public interface GetRoleListByParticipant extends UseCase<GetRoleListByParticipant.Input, GetRoleListByParticipant.Output> {

    record Input() { }

    record Output(List<RoleData> roleList) { }

}

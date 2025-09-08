package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GetOrganizationList extends UseCase<GetOrganizationList.Input, GetOrganizationList.Output> {

    record  Input( ) {}

    record Output(List<OrganizationInfo> organizationInfoList) {
        public record OrganizationInfo(ParticipantId participantId,
                                       String participantName){}
    }

}

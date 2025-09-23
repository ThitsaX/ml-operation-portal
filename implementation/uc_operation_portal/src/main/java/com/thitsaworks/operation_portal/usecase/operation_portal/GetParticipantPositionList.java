package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;

import java.util.List;

public interface GetParticipantPositionList extends
                                  UseCase<GetParticipantPositionList.Input, GetParticipantPositionList.Output> {

    record Input(UserId userId) { }

    record Output(List<FinancialData> financialData) { }

}

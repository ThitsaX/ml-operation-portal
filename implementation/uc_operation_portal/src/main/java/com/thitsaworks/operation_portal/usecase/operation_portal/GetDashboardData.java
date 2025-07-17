package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;

import java.util.List;

public interface GetDashboardData extends
                                  UseCase<GetDashboardData.Input, GetDashboardData.Output> {

    record Input(ParticipantUserId participantUserId) { }

    record Output(List<FinancialData> financialData) { }

}

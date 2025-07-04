package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.FinancialData;

import java.util.List;

public interface GetDashboardData extends
                                  UseCase<GetDashboardData.Input, GetDashboardData.Output> {

    record Input(ParticipantUserId participantUserId) { }

    record Output(List<FinancialData> financialData) { }

}

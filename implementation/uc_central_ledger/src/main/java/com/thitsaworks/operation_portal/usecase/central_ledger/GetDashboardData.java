package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.FinancialData;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import java.util.List;

public abstract class GetDashboardData extends
        AbstractOwnableUseCase<GetDashboardData.Input, GetDashboardData.Output> {

    public record Input(ParticipantUserId participantUserId) {}

    public record Output(List<FinancialData> financialData) {}
}

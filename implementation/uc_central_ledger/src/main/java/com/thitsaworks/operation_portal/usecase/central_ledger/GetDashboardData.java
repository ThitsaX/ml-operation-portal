package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.FinancialData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class GetDashboardData extends
                                       AbstractOwnableUseCase<GetDashboardData.Input, GetDashboardData.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantUserId participantUserId;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<FinancialData> financialData;

    }

}

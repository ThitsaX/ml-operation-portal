package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.ledger.data.FinancialData;
import com.thitsaworks.operation_portal.component.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
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

package com.thitsa.dfsp_portal.usecase.central_ledger;

import com.thitsa.dfsp_portal.ledger.data.TransferData;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

public abstract class GetAllTransfer extends
        AbstractAuditableUseCase<GetAllTransfer.Input, GetAllTransfer.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String fromDate;

        private String toDate;

        private String transferId;

        private String payerFspId;

        private String payeeFspId;

        private String payerIdentifierTypeId;

        private String payeeIdentifierTypeId;

        private String payerIdentifierValue;

        private String payeeIdentifierValue;

        private String currencyId;

        private String transferStateId;

        private ParticipantUserId participantUserId;

        private String timeZone;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<TransferData> transferInfoList;

    }

}

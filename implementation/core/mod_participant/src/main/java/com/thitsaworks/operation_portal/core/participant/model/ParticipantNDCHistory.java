package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCHistoryId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_participant_ndc_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantNDCHistory extends JpaEntity<ParticipantNDCHistoryId> {

    @EmbeddedId
    protected ParticipantNDCHistoryId participantNDCHistoryId;

    @ManyToOne()
    @JoinColumn(name = "participant_ndc_id")
    protected ParticipantNDC participantNDC;

    @Column(name = "dfsp_code")
    protected String dfspCode;

    @Column(name = "currency")
    protected String currency;

    @Column(
        name = "ndc_percent",
        precision = 5,
        scale = 4)
    protected BigDecimal ndcPercent;

    @Column(name = "ndc_amount", precision = 5, scale = 4)
    protected BigDecimal ndcAmount;

    public ParticipantNDCHistory(ParticipantNDC participantNDC,
                                 String dfspCode,
                                 String currency,
                                 BigDecimal ndcPercent,
                                 BigDecimal ndcAmount) {

        this.participantNDCHistoryId = new ParticipantNDCHistoryId(Snowflake.get()
                                                                            .nextId());
        this.participantNDC(participantNDC);
        this.dfspCode(dfspCode);
        this.currency(currency);
        this.ndcPercent(ndcPercent);
        this.ndcAmount(ndcAmount);

    }

    public void participantNDC(ParticipantNDC participantNDC) {

        this.participantNDC = participantNDC;
    }

    public void dfspCode(String dfspCode) {

        this.dfspCode = dfspCode;
    }

    public void currency(String currency) {

        this.currency = currency;
    }

    public void ndcPercent(BigDecimal ndcPercent) {

        this.ndcPercent = ndcPercent;
    }

    public void ndcAmount(BigDecimal ndcAmount) {

        this.ndcAmount = ndcAmount;
    }

    @Override
    public ParticipantNDCHistoryId getId() {

        return this.participantNDCHistoryId;
    }

}

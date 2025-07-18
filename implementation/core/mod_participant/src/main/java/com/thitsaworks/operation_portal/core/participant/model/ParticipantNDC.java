package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_participant_ndc")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantNDC extends JpaEntity<ParticipantNDCId> {

    @EmbeddedId
    protected ParticipantNDCId participantNDCId;

    @Column(name = "participantName")
    protected String dfspCode;

    @Column(name = "currency")
    protected String currency;

    @Column(
        name = "ndc_percent",
        precision = 5,
        scale = 4)
    protected BigDecimal ndcPercent;

    @Column(
            name = "ndc_amount",
            precision = 5,
            scale = 4)
    protected BigDecimal ndcAmount;

    public ParticipantNDC(String dfspCode,
                          String currency,
                          BigDecimal ndcPercent,
                          BigDecimal ndcAmount) {

        this.participantNDCId = new ParticipantNDCId(Snowflake.get()
                                                              .nextId());
        this.dfspCode(dfspCode);
        this.currency(currency);
        this.ndcPercent(ndcPercent);
        this.ndcAmount(ndcAmount);
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
    public ParticipantNDCId getId() {

        return this.participantNDCId;
    }

}

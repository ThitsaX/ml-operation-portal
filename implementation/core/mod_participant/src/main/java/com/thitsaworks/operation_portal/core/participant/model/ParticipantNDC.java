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

    @Column(name = "dfsp")
    protected String dfsp;

    @Column(name = "currency")
    protected String currency;

    @Column(
        name = "ndc_percent",
        precision = 5,
        scale = 4)
    protected BigDecimal ndcPercent;

    public ParticipantNDC(String dfsp,
                          String currency,
                          BigDecimal ndcPercent) {

        this.participantNDCId = new ParticipantNDCId(Snowflake.get()
                                                              .nextId());
        this.dfsp(dfsp);
        this.currency(currency);
        this.ndcPercent(ndcPercent);
    }

    public void dfsp(String dfsp) {

        this.dfsp = dfsp;
    }

    public void currency(String currency) {

        this.currency = currency;
    }

    public void ndcPercent(BigDecimal ndcPercent) {

        this.ndcPercent = ndcPercent;
    }

    @Override
    public ParticipantNDCId getId() {

        return this.participantNDCId;
    }

}

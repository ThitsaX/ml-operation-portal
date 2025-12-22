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
import java.time.Instant;

@Entity
@Table(name = "tbl_participant_ndc")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantNDC extends JpaEntity<ParticipantNDCId> {

    @EmbeddedId
    protected ParticipantNDCId participantNDCId;

    @Column(name = "participant_name")
    protected String participantName;

    @Column(name = "currency")
    protected String currency;

    @Column(
        name = "ndc_percent",
            precision = 7,
        scale = 4)
    protected BigDecimal ndcPercent;

    public ParticipantNDC(String participantName,
                          String currency,
                          BigDecimal ndcPercent ) {

        this.participantNDCId = new ParticipantNDCId(Snowflake.get()
                                                              .nextId());
        this.participantName(participantName);
        this.currency(currency);
        this.ndcPercent(ndcPercent);
    }

    public ParticipantNDC participantName(String participantName) {

        this.participantName = participantName;
        return this;
    }

    public ParticipantNDC currency(String currency) {

        this.currency = currency;
        return this;
    }

    public ParticipantNDC ndcPercent(BigDecimal ndcPercent) {

        this.ndcPercent = ndcPercent;
        return this;
    }

    public ParticipantNDC updatedAt() {

        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public ParticipantNDCId getId() {

        return this.participantNDCId;
    }

}

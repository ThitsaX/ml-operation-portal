package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_participant_ndc")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantNDC extends JpaEntity<ParticipantNDCId> {

    @EmbeddedId
    protected ParticipantNDCId participantNDCId;

    @Column(name = "dfsp_code")
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

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "participantNDC", orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    protected Set<ParticipantNDCHistory> participantNDCHistories = new HashSet<>();

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

    public ParticipantNDC dfspCode(String dfspCode) {

        this.dfspCode = dfspCode;
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

    public ParticipantNDC ndcAmount(BigDecimal ndcAmount) {

        this.ndcAmount = ndcAmount;
        return this;
    }

    public ParticipantNDCHistory moveParticipantNDCToHistory(ParticipantNDC participantNDC) {

        ParticipantNDCHistory participantNDCHistory =
                new ParticipantNDCHistory(participantNDC);

        this.participantNDCHistories.add(participantNDCHistory);
        return participantNDCHistory;

    }

    @Override
    public ParticipantNDCId getId() {

        return this.participantNDCId;
    }

}

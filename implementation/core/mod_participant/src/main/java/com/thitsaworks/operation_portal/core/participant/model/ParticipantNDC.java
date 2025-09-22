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

    @Column(name = "participant_name")
    protected String participantName;

    @Column(name = "currency")
    protected String currency;

    @Column(
        name = "ndc_percent",
            precision = 7,
        scale = 4)
    protected BigDecimal ndcPercent;


    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "participantNDC", orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    protected Set<ParticipantNDCHistory> participantNDCHistories = new HashSet<>();

    public ParticipantNDC(String participantName,
                          String currency,
                          BigDecimal ndcPercent ) {

        this.participantNDCId = new ParticipantNDCId(Snowflake.get()
                                                              .nextId());
        this.dfspCode(participantName);
        this.currency(currency);
        this.ndcPercent(ndcPercent);
    }

    public ParticipantNDC dfspCode(String dfspCode) {

        this.participantName = dfspCode;
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

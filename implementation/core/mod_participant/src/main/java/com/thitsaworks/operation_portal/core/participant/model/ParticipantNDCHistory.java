package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCHistoryId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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

    // @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    //@JoinColumn(name = "participant_ndc_id")
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "participant_ndc_id"))
    protected ParticipantNDCId participantNDCId;

    @Column(name = "participant_name")
    protected String participantName;

    @Column(name = "currency")
    protected String currency;

    @Column(name = "ndc_percent", precision = 7, scale = 4)
    protected BigDecimal ndcPercent;

    public ParticipantNDCHistory(ParticipantNDC participantNDC) {

        this.participantNDCHistoryId = new ParticipantNDCHistoryId(Snowflake.get()
                                                                            .nextId());
        this.participantNDCId(participantNDC.getParticipantNDCId());
        this.participantName(participantNDC.getParticipantName());
        this.currency(participantNDC.getCurrency());
        this.ndcPercent(participantNDC.getNdcPercent());
        this.setCreatedAt(participantNDC.getCreatedAt());
        this.setUpdatedAt(participantNDC.getUpdatedAt());

    }

    public void participantNDCId(ParticipantNDCId participantNDCId) {

        this.participantNDCId = participantNDCId;
    }

    public void participantName(String participantName) {

        this.participantName = participantName;
    }

    public void currency(String currency) {

        this.currency = currency;
    }

    public void ndcPercent(BigDecimal ndcPercent) {

        this.ndcPercent = ndcPercent;
    }

    @Override
    public ParticipantNDCHistoryId getId() {

        return this.participantNDCHistoryId;
    }

}

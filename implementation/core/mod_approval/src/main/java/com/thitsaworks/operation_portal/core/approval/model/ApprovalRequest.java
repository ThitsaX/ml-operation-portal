package com.thitsaworks.operation_portal.core.approval.model;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaInstantConverter;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.approval.exception.ApprovalErrors;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tbl_approval_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalRequest extends JpaEntity<ApprovalRequestId> {

    @EmbeddedId
    protected ApprovalRequestId approvalRequestId;

    @Column(name = "requested_action")
    protected String requestedAction;

    @Column(name = "participant_name")
    protected String participantName;

    @Column(name = "participant_currency")
    protected String participantCurrency;

    @Column(name = "participant_settlement_currency_id")
    protected String participantSettlementCurrencyId;

    @Column(name = "participant_position_currency_id")
    protected String participantPositionCurrencyId;

    @Column(name = "amount")
    protected BigDecimal amount;

    @Embedded
    @AttributeOverride(
        name = "id",
        column = @Column(name = "requested_by"))
    protected UserId requestedBy;

    @Embedded
    @AttributeOverride(
        name = "id",
        column = @Column(name = "responded_by"))
    protected UserId respondedBy;

    @Column(name = "requested_dtm")
    @Convert(converter = JpaInstantConverter.class)
    protected Instant requestedDtm;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    protected ApprovalActionType action;

    public ApprovalRequest(String requestedAction,
                           String participantName,
                           String participantCurrency,
                           String participantSettlementCurrencyId,
                           String participantPositionCurrencyId,
                           BigDecimal amount,
                           UserId requestedBy) {

        this.approvalRequestId = new ApprovalRequestId(Snowflake.get()
                                                                .nextId());
        this.requestedAction(requestedAction);
        this.participantName(participantName);
        this.participantCurrency(participantCurrency);
        this.participantSettlementCurrencyId(participantSettlementCurrencyId);
        this.participantPositionCurrencyId(participantPositionCurrencyId);
        this.amount(amount);
        this.requestedBy(requestedBy);
        this.requestedDtm();
        this.action(ApprovalActionType.PENDING);

    }

    public void requestedAction(String requestedAction) {

        if (requestedAction == null || requestedAction.isBlank()) {
            throw new InputException(ApprovalErrors.INVALID_REQUESTED_ACTION);
        }

        this.requestedAction = requestedAction;
    }

    public void participantName(String participantName) {

        if (participantName == null || participantName.isBlank()) {
            throw new InputException(ApprovalErrors.INVALID_DFSP);
        }

        this.participantName = participantName;
    }

    public void participantCurrency(String participantCurrency) {

        if (participantCurrency == null || participantCurrency.isBlank()) {
            throw new InputException(ApprovalErrors.INVALID_CURRENCY);

        }

        this.participantCurrency = participantCurrency;
    }

    public void participantSettlementCurrencyId(String participantSettlementCurrencyId) {

        if (participantSettlementCurrencyId == null || participantSettlementCurrencyId.isBlank()) {
            throw new InputException(ApprovalErrors.INVALID_CURRENCY);

        }

        this.participantSettlementCurrencyId = participantSettlementCurrencyId;
    }

    public void participantPositionCurrencyId(String participantPositionCurrencyId) {

        if (participantPositionCurrencyId == null || participantPositionCurrencyId.isBlank()) {
            throw new InputException(ApprovalErrors.INVALID_CURRENCY);

        }

        this.participantPositionCurrencyId = participantPositionCurrencyId;
    }

    public void amount(BigDecimal amount) {

        if (amount == null) {
            throw new InputException(ApprovalErrors.INVALID_AMOUNT);
        }

        this.amount = amount;
    }

    public void requestedBy(UserId requestedBy) {

        if (requestedBy == null) {
            throw new InputException(ApprovalErrors.INVALID_REQUESTED_BY);
        }

        this.requestedBy = requestedBy;
    }

    public void respondedBy(UserId respondedBy) {

        if (respondedBy == null) {
            throw new InputException(ApprovalErrors.INVALID_RESPONDED_BY);
        }

        this.respondedBy = respondedBy;
    }

    public void requestedDtm() {

        this.requestedDtm = Instant.now();
    }

    public void action(ApprovalActionType action) {

        this.action = action;
    }

    @Override
    public ApprovalRequestId getId() {

        return this.approvalRequestId;
    }

}

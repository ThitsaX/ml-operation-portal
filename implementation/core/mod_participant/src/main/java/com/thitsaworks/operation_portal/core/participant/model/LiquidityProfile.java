package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_liquidity_profile")
@Getter
@NoArgsConstructor
public class LiquidityProfile extends JpaEntity<LiquidityProfileId> {

    @EmbeddedId
    protected LiquidityProfileId liquidityProfileId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "participant_id")
    protected Participant participant;

    @Column(name = "account_name")
    protected String accountName;

    @Column(name = "account_number")
    protected String accountNumber;

    @Column(name = "currency")
    protected String currency;

    @Column(name = "is_active")
    protected Boolean isActive;

    public LiquidityProfile(Participant participant, String accountName, String accountNumber, String currency,
                            Boolean isActive) {

        Validate.notNull(participant);

        this.liquidityProfileId = new LiquidityProfileId(Snowflake.get().nextId());
        this.participant = participant;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.isActive = isActive;
    }

    @Override
    public LiquidityProfileId getId() {

        return this.liquidityProfileId;
    }

    public LiquidityProfile accountName(String accountName) {

        this.accountName = accountName;
        return this;

    }

    public LiquidityProfile accountNumber(String accountNumber) {

        this.accountNumber = accountNumber;
        return this;

    }

    public LiquidityProfile currency(String currency) {

        this.currency = currency;
        return this;

    }

    public LiquidityProfile isActive(Boolean isActive) {

        this.isActive = isActive;
        return this;

    }

}

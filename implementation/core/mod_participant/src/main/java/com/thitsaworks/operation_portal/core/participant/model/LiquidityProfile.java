package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.participant.cache.LiquidityProfileCache;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@Entity
@EntityListeners(value = {LiquidityProfileCache.Updater.class})
@Table(name = "tbl_liquidity_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LiquidityProfile extends JpaEntity<LiquidityProfileId> {

    @EmbeddedId
    protected LiquidityProfileId liquidityProfileId;

    @ManyToOne()
    @JoinColumn(name = "participant_id")
    protected Participant participant;

    @Column(name = "bank_name")
    protected String bankName;

    @Column(name = "account_name")
    protected String accountName;

    @Column(name = "account_number")
    protected String accountNumber;

    @Column(name = "currency")
    protected String currency;

    @Column(name = "is_active")
    protected Boolean isActive;

    public LiquidityProfile(Participant participant,
                            String bankName,
                            String accountName,
                            String accountNumber,
                            String currency,
                            Boolean isActive) {

        Validate.notNull(participant);

        this.liquidityProfileId = new LiquidityProfileId(Snowflake.get()
                                                                  .nextId());
        this.bankName = bankName;
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

    public LiquidityProfile bankName(String bankName) {

        this.bankName = bankName;
        return this;
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

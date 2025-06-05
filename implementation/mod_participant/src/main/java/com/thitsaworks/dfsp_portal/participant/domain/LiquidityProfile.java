package com.thitsaworks.dfsp_portal.participant.domain;

import com.thitsaworks.dfsp_portal.component.data.jpa.JpaEntity;
import com.thitsaworks.dfsp_portal.component.util.Snowflake;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    protected LiquidityProfileId getPrimaryId() {

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

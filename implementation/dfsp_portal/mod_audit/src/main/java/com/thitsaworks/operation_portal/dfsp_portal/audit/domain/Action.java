package com.thitsaworks.operation_portal.dfsp_portal.audit.domain;

import com.thitsaworks.operation_portal.component.misc.data.jpa.JpaEntity;
import com.thitsaworks.operation_portal.dfsp_portal.audit.identity.ActionId;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_action")
@Getter
@NoArgsConstructor
public class Action extends JpaEntity<ActionId> {

    @EmbeddedId
    protected ActionId actionId;

    @Column(name = "name")
    protected String name;

    public Action(String name) {

        this.actionId = new ActionId(Snowflake.get().nextId());
        this.name = name;
    }

    @Override
    protected ActionId getPrimaryId() {

        return this.actionId;
    }

}

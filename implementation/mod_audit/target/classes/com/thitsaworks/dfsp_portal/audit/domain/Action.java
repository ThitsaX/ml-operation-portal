package com.thitsaworks.dfsp_portal.audit.domain;

import com.thitsaworks.dfsp_portal.audit.identity.ActionId;
import com.thitsaworks.dfsp_portal.component.data.jpa.JpaEntity;
import com.thitsaworks.dfsp_portal.component.util.Snowflake;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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

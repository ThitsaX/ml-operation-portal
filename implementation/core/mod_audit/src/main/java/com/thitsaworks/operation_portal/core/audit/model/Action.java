package com.thitsaworks.operation_portal.core.audit.model;

import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.component.common.identifier.ActionId;
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
    public ActionId getId() {

        return this.actionId;
    }

}

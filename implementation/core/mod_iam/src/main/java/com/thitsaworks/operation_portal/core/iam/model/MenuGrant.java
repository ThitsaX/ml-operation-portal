package com.thitsaworks.operation_portal.core.iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_menu_grant")
@NoArgsConstructor
@Getter
public class MenuGrant extends JpaEntity<GrantId> {

    @EmbeddedId
    protected GrantId GrantId;

    @ManyToOne
    @JoinColumn(name = "action_id")
    protected Action Action;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    protected Menu menu;

    public MenuGrant(Action Action, Menu menu) {

        this.GrantId =new GrantId(Snowflake.get().nextId());
        this.Action = Action;
        this.menu = menu;
    }

    @Override
    public GrantId getId() {

        return this.GrantId;
    }

}

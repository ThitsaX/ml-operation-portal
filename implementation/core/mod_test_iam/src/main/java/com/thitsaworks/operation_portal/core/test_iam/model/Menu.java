package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.MenuId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tbl_menu")
@NoArgsConstructor
@Getter
public class Menu extends JpaEntity<MenuId> {

    @EmbeddedId
    protected MenuId menuId;

    @Column(name = "name")
    protected String name;

    @Column(name ="parent_id")
    protected String  parentId;

    @Column(name = "is_active")
    protected boolean isActive;

    public Menu(String name, String parentId, boolean isActive) {

        this.menuId = new MenuId(Snowflake.get()
                                          .nextId());
        this.name = name;
        this.parentId = parentId;
        this.isActive = isActive;
    }

    @Override
    public MenuId getId() {

        return this.menuId;
    }

}
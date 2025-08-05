package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "tbl_menu")
@NoArgsConstructor
@Getter
public class Menu extends JpaEntity<MenuId> {

    @EmbeddedId
    protected MenuId menuId;

    @Column(name = "name")
    protected String name;

    @Column(name = "parent_id")
    protected String parentId;

    @Column(name = "is_active")
    protected boolean isActive;

    @Getter(AccessLevel.NONE)
    @OneToMany(
        mappedBy = "menu",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<MenuGrant> grants = new HashSet<>();

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

    public boolean isGranted(IAMAction IAMAction) {

        return this.grants.stream()
                          .anyMatch(granted -> granted.IAMAction.equals(IAMAction));
    }

    public void grantAction(IAMAction granting) {

        Optional<MenuGrant> optMenuGrant =
            this.grants.stream()
                       .filter(menuGrant -> menuGrant.IAMAction.equals(granting))
                       .findFirst();

        if (optMenuGrant.isEmpty()) {

            this.grants.add(new MenuGrant(granting, this));
        }
    }

    public boolean revokeAction(IAMAction revoking) {

        Optional<MenuGrant> optMenuGrant =
            this.grants.stream()
                       .filter(menuGrant -> menuGrant.IAMAction.equals(revoking))
                       .findFirst();

        if (optMenuGrant.isPresent()) {

            this.grants.remove(optMenuGrant.get());
            return true;
        }

        return false;
    }

}
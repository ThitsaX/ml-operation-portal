package com.thitsaworks.operation_portal.core.iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.listener.RoleListener;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@EntityListeners(RoleListener.class)
@Table(name = "tbl_role")
@NoArgsConstructor
@Getter
public class Role extends JpaEntity<RoleId> {

    @EmbeddedId
    protected RoleId roleId;

    @Column(name = "name")
    protected String name;

    @Column(name = "active")
    protected Boolean active = true;

    @Getter(AccessLevel.NONE)
    @OneToMany(
        mappedBy = "role",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<RoleGrant> grants = new HashSet<>();

    public Role(RoleId roleId, String name) {

        assert name != null : "name is required!";

        this.roleId = roleId;
        this.name(name);
    }

    @Override
    public RoleId getId() {

        return this.roleId;
    }

    public Role name(String name) {

        if (name == null || name.isBlank()) {

            throw new InputException(IAMErrors.INVALID_ROLE_NAME);
        }

        this.name = name;

        return this;
    }

    public boolean isGranted(Action Action) {

        return this.grants.stream()
                          .anyMatch(granted -> granted.Action.equals(Action));
    }

    public void grantAction(Action granting) {

        Optional<RoleGrant> optRoleGrant =
            this.grants.stream()
                       .filter(roleGrant -> roleGrant.Action.equals(granting))
                       .findFirst();

        if (optRoleGrant.isEmpty()) {

            this.grants.add(new RoleGrant(this, granting));
        }
    }

    public Set<Action> getGrantedActions() {

        return this.grants.stream()
                          .map(RoleGrant::getAction)
                          .collect(Collectors.toSet());
    }

    public boolean revokeAction(Action revoking) {

        Optional<RoleGrant> optRoleGrant =
            this.grants.stream()
                       .filter(roleGrant -> roleGrant.Action.equals(revoking))
                       .findFirst();

        if (optRoleGrant.isPresent()) {

            this.grants.remove(optRoleGrant.get());
            return true;
        }

        return false;
    }

    public void toggle() {

        this.active = !this.active;
    }

}


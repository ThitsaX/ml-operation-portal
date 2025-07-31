package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.test_iam.listener.RoleListener;
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

    public Role(String name) {

        assert name != null : "name is required!";

        this.roleId = new RoleId(Snowflake.get()
                                          .nextId());
        this.name(name);
    }

    @Override
    public RoleId getId() {

        return this.roleId;
    }

    public Role name(String name) {

        if (name == null || name.isEmpty() || name.isBlank()) {

//            throw new IAMException(IAMException.ErrorCodes.InvalidName);
        }

        this.name = name;

        return this;
    }

    public boolean isGranted(IAMAction IAMAction) {

        return this.grants.stream()
                          .anyMatch(granted -> granted.IAMAction.equals(IAMAction));
    }

    public void grantAction(IAMAction granting) {

        Optional<RoleGrant> optRoleGrant =
            this.grants.stream()
                       .filter(roleGrant -> roleGrant.IAMAction.equals(granting))
                       .findFirst();

        if (optRoleGrant.isEmpty()) {

            this.grants.add(new RoleGrant(this, granting));
        }
    }

    public Set<IAMAction> getGrantedActions() {

        return this.grants.stream()
                          .map(RoleGrant::getIAMAction)
                          .collect(Collectors.toSet());
    }

    public boolean revokeAction(IAMAction revoking) {

        Optional<RoleGrant> optRoleGrant =
            this.grants.stream()
                       .filter(roleGrant -> roleGrant.IAMAction.equals(revoking))
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


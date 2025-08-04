package com.thitsaworks.operation_portal.core.iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.iam.listener.PrincipalRoleListener;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(PrincipalRoleListener.class)
@Table(name = "tbl_principal_role")
@NoArgsConstructor
@Getter
public class PrincipalRole extends JpaEntity<PrincipalRoleId> {

    @EmbeddedId
    protected PrincipalRoleId principalRoleId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    protected Role role;

    @ManyToOne
    @JoinColumn(name = "principal_id")
    protected Principal principal;

    public PrincipalRole(Role role, Principal principal) {

        assert role != null;
        assert principal != null;

        this.principalRoleId = new PrincipalRoleId(Snowflake.get()
                                                            .nextId());
        this.role = role;
        this.principal = principal;
    }

    @Override
    public PrincipalRoleId getId() {

        return this.principalRoleId;
    }

}


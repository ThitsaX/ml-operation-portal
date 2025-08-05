package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.test_iam.listener.UserRoleListener;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(UserRoleListener.class)
@Table(name = "tbl_user_role")
@NoArgsConstructor
@Getter
public class UserRole extends JpaEntity<PrincipalRoleId> {

    @EmbeddedId
    protected PrincipalRoleId principalRoleId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    protected Role role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User user;

    public UserRole(Role role, User user) {

        assert role != null;
        assert user != null;

        this.principalRoleId = new PrincipalRoleId(Snowflake.get().nextId());
        this.role = role;
        this.user = user;
    }


    @Override
    public PrincipalRoleId getId() {

        return this.principalRoleId;
    }
}


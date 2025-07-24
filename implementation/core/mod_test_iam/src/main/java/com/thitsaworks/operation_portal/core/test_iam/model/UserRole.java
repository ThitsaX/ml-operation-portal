package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.UserRoleId;
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
@Table(name = "tbl_user_role")
@NoArgsConstructor
@Getter
public class UserRole extends JpaEntity<UserRoleId> {

    @EmbeddedId
    protected UserRoleId userRoleId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    protected Role role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected IAMuser user;

    public UserRole(Role role, IAMuser user) {

        assert role != null;
        assert user != null;

        this.userRoleId = new UserRoleId(Snowflake.get().nextId());
        this.role = role;
        this.user = user;
    }


    @Override
    public UserRoleId getId() {

        return this.userRoleId;
    }
}


package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.test_iam.cache.RoleCache;
import com.thitsaworks.operation_portal.core.test_iam.cache.RoleGrantCache;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(value = {RoleGrantCache.Updater.class})
@Table(name = "tbl_role_grant")
@NoArgsConstructor
@Getter
public class RoleGrant extends JpaEntity<GrantId> {
    @EmbeddedId
    protected GrantId grantId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    protected Role role;

    @ManyToOne
    @JoinColumn(name = "action_id")
    protected IAMAction IAMAction;

    public RoleGrant(Role role, IAMAction IAMAction) {

        assert role != null;
        assert IAMAction != null;

        this.grantId = new GrantId(Snowflake.get().nextId());
        this.role = role;
        this.IAMAction = IAMAction;
    }

    @Override
    public GrantId getId() {

        return this.grantId;
    }

}


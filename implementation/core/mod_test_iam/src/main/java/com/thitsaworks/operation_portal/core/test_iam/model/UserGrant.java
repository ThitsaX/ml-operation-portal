package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
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
@Table(name = "tbl_user_grant")
@NoArgsConstructor
@Getter
public class UserGrant extends JpaEntity<GrantId> {

    @EmbeddedId
    protected GrantId grantId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne
    @JoinColumn(name = "action_id")
    protected IAMAction IAMAction;

    public UserGrant(User user, IAMAction IAMAction) {

        assert user != null;
        assert IAMAction != null;

        this.grantId = new GrantId(Snowflake.get().nextId());
        this.user = user;
        this.IAMAction = IAMAction;
    }

    @Override
    public GrantId getId() {

        return this.grantId;
    }


}


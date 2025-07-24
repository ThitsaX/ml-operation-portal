package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.BlockedActionId;
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
@Table(name = "tbl_blocked_action")
@NoArgsConstructor
@Getter
public class BlockedAction extends JpaEntity<BlockedActionId> {
    @EmbeddedId
    protected BlockedActionId blockedActionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne
    @JoinColumn(name = "action_id")
    protected Action action;

    public BlockedAction(User user, Action action) {

        assert user != null;
        assert action != null;

        this.blockedActionId = new BlockedActionId(Snowflake.get().nextId());
        this.user = user;
        this.action = action;
    }

    @Override
    public BlockedActionId getId() {

        return this.blockedActionId;
    }


}

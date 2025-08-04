package com.thitsaworks.operation_portal.core.iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.BlockedActionId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.iam.listener.BlockedActionListener;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(BlockedActionListener.class)
@Table(name = "tbl_blocked_action")
@NoArgsConstructor
@Getter
public class BlockedAction extends JpaEntity<BlockedActionId> {
    @EmbeddedId
    protected BlockedActionId blockedActionId;

    @ManyToOne
    @JoinColumn(name = "principal_id")
    protected Principal principal;

    @ManyToOne
    @JoinColumn(name = "action_id")
    protected Action Action;

    public BlockedAction(Principal principal, Action Action) {

        assert principal != null;
        assert Action != null;

        this.blockedActionId = new BlockedActionId(Snowflake.get().nextId());
        this.principal = principal;
        this.Action = Action;
    }

    @Override
    public BlockedActionId getId() {

        return this.blockedActionId;
    }


}

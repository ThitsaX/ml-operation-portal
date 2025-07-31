package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.test_iam.listener.IAMActionListener;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(IAMActionListener.class)
@Table(name = "iam_action")
@NoArgsConstructor
@Getter
public class IAMAction extends JpaEntity<ActionId> {
    @EmbeddedId
    protected ActionId actionId;

    @Column(name = "action_code")
    @Convert(converter = ActionCode.JpaConverter.class)
    protected ActionCode actionCode;

    @Column(name = "scope")
    protected String scope;

    @Column(name = "description")
    protected String description;

    public IAMAction(ActionCode actionCode, String scope, String description) {

        assert actionCode != null : "actionCode is required!";
        assert scope != null : "scope is required!";

        this.actionId = new ActionId(Snowflake.get().nextId());
        this.actionCode = actionCode;
        this.scope = scope;
        this.description = description;
    }

    public IAMAction description(String description) {

        this.description = description;

        return this;
    }

    @Override
    public ActionId getId() {

        return  this.actionId;
    }

    public IAMAction scope(String scope) {

        this.scope = scope;

        return this;
    }


}



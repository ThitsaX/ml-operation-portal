package com.thitsaworks.operation_portal.core.iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.iam.listener.PrincipalGrantListener;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(PrincipalGrantListener.class)
@Table(name = "tbl_principal_grant")
@NoArgsConstructor
@Getter
public class PrincipalGrant extends JpaEntity<GrantId> {

    @EmbeddedId
    protected GrantId grantId;

    @ManyToOne
    @JoinColumn(name = "principal_id")
    protected Principal principal;

    @ManyToOne
    @JoinColumn(name = "action_id")
    protected Action Action;

    public PrincipalGrant(Principal principal, Action Action) {

        assert principal != null;
        assert Action != null;

        this.grantId = new GrantId(Snowflake.get()
                                            .nextId());
        this.principal = principal;
        this.Action = Action;
    }

    @Override
    public GrantId getId() {

        return this.grantId;
    }

}


package com.thitsaworks.operation_portal.dfsp_portal.audit.domain;

import com.thitsaworks.operation_portal.component.misc.data.jpa.JpaEntity;
import com.thitsaworks.operation_portal.dfsp_portal.audit.identity.ActionId;
import com.thitsaworks.operation_portal.dfsp_portal.audit.identity.AuditId;
import com.thitsaworks.operation_portal.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.RealmId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_audit")
@Getter
@NoArgsConstructor
public class Audit extends JpaEntity<AuditId> {

    @EmbeddedId
    protected AuditId auditId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "action_id"))
    protected ActionId actionId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    protected UserId userId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "participant_id"))
    protected RealmId realmId;

    @Column(name = "input_info")
    protected String inputInfo;

    @Column(name = "output_info")
    protected String outputInfo;

    public Audit(ActionId actionId, UserId userId, RealmId realmId, String inputInfo,
                 String outputInfo) {

        this.auditId = new AuditId(Snowflake.get().nextId());
        this.actionId = actionId;
        this.userId = userId;
        this.realmId = realmId;
        this.inputInfo = inputInfo;
        this.outputInfo = outputInfo;
    }

    @Override
    protected AuditId getPrimaryId() {

        return this.auditId;
    }

}

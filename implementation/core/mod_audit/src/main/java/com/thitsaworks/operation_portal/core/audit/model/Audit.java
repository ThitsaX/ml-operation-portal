package com.thitsaworks.operation_portal.core.audit.model;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.RequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
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
    protected ActionId actionId;

    @Embedded
    protected UserId userId;

    @Embedded
    protected RequestId requestId;

    @Embedded
    @AttributeOverride(
        name = "id",
        column = @Column(name = "participant_id"))
    protected RealmId realmId;

    @Column(name = "input_info")
    protected String inputInfo;

    @Column(name = "output_info")
    protected String outputInfo;

    @Column(name = "exception")
    protected String exception;

    public Audit(ActionId actionId,
                 UserId userId,
                 RealmId realmId,
                 RequestId requestId,
                 String inputInfo,
                 String outputInfo) {

        this.auditId = new AuditId(Snowflake.get()
                                            .nextId());
        this.actionId = actionId;
        this.userId = userId;
        this.realmId = realmId;
        this.requestId = requestId;
        this.inputInfo = inputInfo;
        this.outputInfo = outputInfo;
    }

    @Override
    public AuditId getId() {

        return this.auditId;
    }

    public void outputInfo(String outputInfo) {

        this.outputInfo = outputInfo;
    }

    public void exception(String exception) {

        this.exception = exception;
    }

}

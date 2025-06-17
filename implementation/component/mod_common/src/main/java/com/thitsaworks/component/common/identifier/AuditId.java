package com.thitsaworks.component.common.identifier;

import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AuditId extends JpaId<Long> {

    @Column(name = "audit_id")
    private Long id;

    @Override
    public Long getEntityId() {

        return id;
    }

}
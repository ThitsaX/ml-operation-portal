package com.thitsaworks.operation_portal.component.common.identifier;

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
public class RequestId extends JpaId<Long> {

    @Column(name = "request_id")
    private Long id;

    @Override
    public Long getEntityId() {

        return this.id;
    }

}

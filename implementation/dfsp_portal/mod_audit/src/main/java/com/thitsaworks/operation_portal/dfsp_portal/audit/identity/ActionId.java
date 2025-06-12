package com.thitsaworks.operation_portal.dfsp_portal.audit.identity;

import com.thitsaworks.operation_portal.component.data.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ActionId extends JpaId<Long> {

    @Column(name = "action_id")
    private Long id;

    @Override
    public Long getEntityId() {

        return id;
    }

}

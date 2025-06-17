package com.thitsaworks.component.common.identifier;

import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PrincipalId extends JpaId<Long> {

    @Column(name = "principal_id")
    private Long id;

    @Override
    public Long getEntityId() {

        return id;
    }

}

package com.thitsaworks.component.common.identifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaId;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContactId extends JpaId<Long> {

    @Column(name = "contact_id")
    private Long id;

    @Override
    public Long getEntityId() {

        return id;
    }

}
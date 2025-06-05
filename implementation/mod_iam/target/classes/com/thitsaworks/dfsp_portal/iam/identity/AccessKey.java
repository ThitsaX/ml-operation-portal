package com.thitsaworks.dfsp_portal.iam.identity;

import com.thitsaworks.dfsp_portal.component.data.jpa.JpaId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AccessKey extends JpaId<Long> {

    @Column(name = "access_key")
    private Long id;

    @Override
    public Long getEntityId() {

        return id;
    }

}

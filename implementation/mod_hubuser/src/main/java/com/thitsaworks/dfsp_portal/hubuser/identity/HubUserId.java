package com.thitsaworks.dfsp_portal.hubuser.identity;

import com.thitsaworks.dfsp_portal.component.data.jpa.JpaId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class HubUserId extends JpaId<Long> {

    @Column(name = "user_id")
    private Long id;

    @Override
    public Long getEntityId() {
        return id;
    }
}

package com.thitsaworks.dfsp_portal.hubuser.identity;

import com.thitsaworks.dfsp_portal.component.data.jpa.JpaId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AnnouncementId extends JpaId<Long> {

    @Column(name = "announcement_id")
    private Long id;

    @Override
    public Long getEntityId() {

        return id;
    }

}

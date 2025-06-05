package com.thitsaworks.dfsp_portal.participant.identity;

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
public class ParticipantId extends JpaId<Long> {

    @Column(name = "participant_id")
    private Long id;

    @Override
    public Long getEntityId() {

        return id;
    }

}
package com.thitsaworks.operation_portal.participant.query.data;

import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantData implements Serializable {

    protected ParticipantId participantId;

    protected String name;

    protected DfspCode dfspCode;

    protected String address;

    protected Mobile mobile;

    protected ContactId businessContactId;

    protected ContactId technicalContactId;

    @Setter
    protected List<ParticipantUserId> participantUserIds;

    public ParticipantData(Participant participant) {

        this.participantId = participant.getParticipantId();
        this.name = participant.getName();
        this.dfspCode = participant.getDfspCode();
        this.mobile = participant.getMobile();
        this.businessContactId =
                participant.getBusinessContact() != null ? participant.getBusinessContact().getContactId() : null;
        this.technicalContactId =
                participant.getTechnicalContact() != null ? participant.getTechnicalContact().getContactId() : null;

    }

}

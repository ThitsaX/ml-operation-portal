package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import com.thitsaworks.operation_portal.core.participant.exception.LiquidityProfileNotFoundException;

import java.util.List;

public interface LiquidityProfileQuery {

    List<LiquidityProfileData> getLiquidityProfiles(ParticipantId participantId);

    LiquidityProfileData get(LiquidityProfileId contactId) throws LiquidityProfileNotFoundException;

}


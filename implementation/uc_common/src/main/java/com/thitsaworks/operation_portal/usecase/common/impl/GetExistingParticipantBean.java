package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.query.GetContacts;
import com.thitsaworks.operation_portal.participant.query.GetLiquidityProfiles;
import com.thitsaworks.operation_portal.participant.query.GetParticipant;
import com.thitsaworks.operation_portal.usecase.common.GetExistingParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetExistingParticipantBean extends GetExistingParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantBean.class);

    @Autowired
    private GetParticipant getParticipant;

    @Autowired
    private GetContacts getContacts;

    @Autowired
    private GetLiquidityProfiles getLiquidityProfiles;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @DfspWriteTransactional
    public GetExistingParticipant.Output onExecute(GetExistingParticipant.Input input) throws
            ParticipantNotFoundException {

        GetParticipant.Output output =
                this.getParticipant.execute(new GetParticipant.Input(input.getParticipantId()));

        GetContacts.Output contact = this.getContacts.execute(new GetContacts.Input(input.getParticipantId()));

        List<Output.ContactInfo> contactInfoList = new ArrayList<>();

        for (GetContacts.Output.ContactInfo data : contact.getContactInfoList()) {

            contactInfoList.add(
                    new Output.ContactInfo(data.getContactId(), data.getName(), data.getTitle(), data.getEmail(),
                            data.getMobile(), data.getContactType()));
        }

        GetLiquidityProfiles.Output liquidityProfiles =
                this.getLiquidityProfiles.execute(new GetLiquidityProfiles.Input(input.getParticipantId()));

        List<Output.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (GetLiquidityProfiles.Output.LiquidityProfileInfo data : liquidityProfiles.getLiquidityProfileInfoList()) {

            liquidityProfileInfoList.add(
                    new Output.LiquidityProfileInfo(data.getLiquidityProfileId(), data.getAccountName(),
                            data.getAccountNumber(), data.getCurrency(), data.getIsActive()));
        }

        GetExistingParticipant.Output result =
                new GetExistingParticipant.Output(output.getParticipantId(),
                        output.getDfsp_code(),
                        output.getName(),
                        output.getAddress(),
                        output.getMobile(),
                        output.getCreatedDate(),
                        contactInfoList,
                        liquidityProfileInfoList);

        return result;
    }

    @Override
    protected String getName() {

        return GetExistingParticipant.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_common";
    }

    @Override
    protected String getId() {

        return GetExistingParticipant.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(Long.parseLong(securityContext.getAccessKey())));

        switch (principalData.getUserRoleType()) {

            case OPERATION:
            case ADMIN:
                return true;
            case SUPERUSER:
            case REPORTING:
                return false;
        }

        return false;
    }

    @Override
    public void onAudit(GetExistingParticipant.Input input, GetExistingParticipant.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetExistingParticipant.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}

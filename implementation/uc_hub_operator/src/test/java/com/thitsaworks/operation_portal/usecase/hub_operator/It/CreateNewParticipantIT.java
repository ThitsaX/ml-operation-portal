package com.thitsaworks.operation_portal.usecase.hub_operator.It;

import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_operator.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewParticipant;
import com.thitsaworks.operation_portal.usecase.hub_operator.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                HubOperatorUseCaseConfiguration.class, TestSettings.class})
public class CreateNewParticipantIT extends BaseVaultSetUpTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewParticipantIT.class);

    @Autowired
    private CreateNewParticipant createNewParticipant;

    @Test
    public void success() throws DomainException {
        var input = new CreateNewParticipant.Input(
                "John Doe",
                new DfspCode("jeew"),
                "DFSP Name",
                "Yangon",
                new Mobile("1234567890"),
                List.of(
                        new CreateNewParticipant.Input.ContactInfo(
                                "Jane Doe",
                                "Manager",
                                new Email("njjaa@gmail.com"),
                                new Mobile("0987654321"),
                                ContactType.BUSINESS
                        )
                ),
                List.of(
                        new CreateNewParticipant.Input.LiquidityProfileInfo(
                                "Account 1",
                                "123456789",
                                "MMK",
                                true
                        )
                )
        );
        var output = this.createNewParticipant.execute(input);
       LOGGER.info("Boolean {},participantId -[{}]",output.created(),output.participantId());
    }
}

package com.thitsaworks.operation_portal.core.approval.command;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.approval.ApprovalConfiguration;
import com.thitsaworks.operation_portal.core.approval.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.approval.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        ApprovalConfiguration.class, TestSettings.class})
public class CreateApprovalRequestCommandIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateApprovalRequestCommandIT.class);

    @Autowired
    private CreateApprovalRequestCommand createApprovalRequestCommand;

    @Test
    public void success() {

        LOG.info("Approval Request : [{}]",
                 this.createApprovalRequestCommand.execute(new CreateApprovalRequestCommand.Input("Deposit",
                                                                                                  "wallet2",
                                                                                                  "USD",
                                                                                                  "8",
                                                                                                  "9",
                                                                                                  BigDecimal.valueOf(
                                                                                                      50000.00),
                                                                                                  new UserId(
                                                                                                      728941546990530560L))));
    }

}

package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerDbSettings;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetTransferDetails;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerUseCaseConfiguration.class, CentralLedgerDbSettings.class})
public class GetTransferDetailsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailsUnitTest.class);

    @Autowired
    private GetTransferDetails getTransferDetails;

    @Test
    public void test_getTransferDetailsSuccessfully() throws Exception {

        var output = this.getTransferDetails.execute(
                new GetTransferDetails.Input("4e573c61-3129-4c2a-af39-92cb1b6d6358"));

        if (output.getBusinessData() != null) {
            LOG.info(output.getBusinessData().getTransferId() + " , " +
                    output.getBusinessData().getAmount() + " , " +
                    output.getBusinessData().getCurrency() + " , " +
                    output.getBusinessData().getPayerDfsp() + " , " +
                    output.getBusinessData().getPayeeDfsp() + " , " +
                    output.getBusinessData().getState() + " , " +
                    output.getBusinessData().getType() + " , " +
                    output.getBusinessData().getSettlementBatch()
            );
        } else {
            LOG.info("No record");
        }
    }

}

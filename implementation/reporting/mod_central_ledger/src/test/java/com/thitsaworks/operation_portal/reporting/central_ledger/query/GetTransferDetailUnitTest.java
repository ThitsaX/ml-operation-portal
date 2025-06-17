package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.CentralLedgerConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingJpaPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerConfiguration.class, ReportingJpaPersistenceConfiguration.class})
public class GetTransferDetailUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailUnitTest.class);

    @Autowired
    private GetTransferDetail getTransferDetail;

    @Test
    public void test_getTransferDetailSuccessfully() throws Exception {

        GetTransferDetail.Output output = this.getTransferDetail.execute(
                new GetTransferDetail.Input("4e573c61-3129-4c2a-af39-92cb1b6d6358"));

        if (output.getBusinessData() != null) {

            LOG.info(output.getBusinessData().getTransferId() + " , " +
                    output.getBusinessData().getAmount() + " , " +
                    output.getBusinessData().getCurrency() + " , " +
                    output.getBusinessData().getPayer() + " , " +
                    output.getBusinessData().getPayerDetails() + " , " +
                    output.getBusinessData().getPayerDfsp() + " , " +
                    output.getBusinessData().getPayee() + " , " +
                    output.getBusinessData().getPayeeDetails() + " , " +
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

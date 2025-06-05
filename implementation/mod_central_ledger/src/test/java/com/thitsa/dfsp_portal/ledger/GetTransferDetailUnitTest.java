package com.thitsa.dfsp_portal.ledger;

import com.thitsa.dfsp_portal.ledger.persistence.CentralLedgerDBSetting;
import com.thitsa.dfsp_portal.ledger.query.GetTransferDetail;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerConfiguration.class, CentralLedgerReadDbConfiguration.class, CentralLedgerDBSetting.class})
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

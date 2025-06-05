package com.thitsa.dfsp_portal.usecase;

import com.thitsa.dfsp_portal.datasource.persistence.CentralLedgerDbSettings;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetAllTransfer;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = {
        CentralLedgerUseCaseConfiguration.class, CentralLedgerDbSettings.class})
public class GetAllTransferUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferUnitTest.class);

    @Autowired
    private GetAllTransfer getAllTransfer;

    @Test
    public void test_getAllTransferSuccessfully() throws Exception {

        var output = this.getAllTransfer.execute(new GetAllTransfer.Input("2022-01-20T10:56:15.00Z",
                "2022-06-20T10:56:15.00Z", "4e573c61-3129-4c2a-af39-92cb1b6d6358", null, null, null,
                null, null, null, null, null, null,null
        ));

        if (output.getTransferInfoList() != null && output.getTransferInfoList().size() > 0) {
            for (var obj : output.getTransferInfoList())
                LOG.info(obj.getTransferId() + " , " + obj.getAmount() + " , " + obj.getCurrency() + " , " +
                        obj.getPayerDfsp() + " , " + obj.getPayeeDfsp() + " , " + obj.getState() + " , " +
                        obj.getType() + " , " +
                        obj.getSettlementBatch()
                );
        } else {
            LOG.info("No record");
        }
    }

}

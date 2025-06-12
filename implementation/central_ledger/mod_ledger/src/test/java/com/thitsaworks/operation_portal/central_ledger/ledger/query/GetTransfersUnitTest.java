package com.thitsaworks.operation_portal.central_ledger.ledger.query;

import com.thitsaworks.operation_portal.central_ledger.ledger.CentralLedgerConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.CentralLedgerJpaPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerConfiguration.class, CentralLedgerJpaPersistenceConfiguration.class})
public class GetTransfersUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransfersUnitTest.class);

    @Autowired
    private GetTransfers getTransfers;

    @Test
    public void test_getTransfersSuccessfully() throws Exception {

        GetTransfers.Output output = this.getTransfers.execute(
                new GetTransfers.Input("2022-01-20T10:56:15.00Z",
                        "2022-06-20T10:56:15.00Z",
                        "4e573c61-3129-4c2a-af39-92cb1b6d6358",
                        null, null, null,
                        null, null, null,
                        null, null, null,"0630"));

        if (output.getTransferInfoList() != null && output.getTransferInfoList().size() > 0) {
            for (var obj : output.getTransferInfoList())
                LOG.info(obj.getTransferId() + " , " +
                        obj.getAmount() + " , " +
                        obj.getCurrency() + " , " +
                        obj.getPayerDfsp() + " , " +
                        obj.getPayeeDfsp() + " , " +
                        obj.getState() + " , " +
                        obj.getType() + " , " +
                        obj.getSettlementBatch()
                );
        } else {
            LOG.info("No record");
        }

    }

}

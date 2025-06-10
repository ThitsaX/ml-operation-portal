package com.thitsaworks.operation_portal.ledger;

import com.thitsaworks.operation_portal.ledger.persistence.CentralLedgerDBSetting;
import com.thitsaworks.operation_portal.ledger.query.GetTransferStates;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerConfiguration.class, CentralLedgerReadDbConfiguration.class, CentralLedgerDBSetting.class})
public class GetTransferStatesUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferStatesUnitTest.class);

    @Autowired
    private GetTransferStates getTransferStates;

    @Test
    public void test_getTransferStatesSuccessfully() throws Exception {

        GetTransferStates.Output output = this.getTransferStates.execute(
                new GetTransferStates.Input());

        if (output.getTransferStateDataList() != null && output.getTransferStateDataList().size() > 0) {
            for (var obj : output.getTransferStateDataList())
                LOG.info(obj.getTransferStateId() + " , " +
                        obj.getTransferState()
                );
        } else {
            LOG.info("No record");
        }

    }

}

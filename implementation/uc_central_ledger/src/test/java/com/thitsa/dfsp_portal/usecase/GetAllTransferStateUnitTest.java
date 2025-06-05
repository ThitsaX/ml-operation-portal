package com.thitsa.dfsp_portal.usecase;

import com.thitsa.dfsp_portal.datasource.persistence.CentralLedgerDbSettings;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetAllTransferState;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerUseCaseConfiguration.class, CentralLedgerDbSettings.class})
public class GetAllTransferStateUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferStateUnitTest.class);

    @Autowired
    private GetAllTransferState getAllTransferState;

    @Test
    public void test_getAllTransferStateSuccessfully() throws Exception {

        var output = this.getAllTransferState.execute(
                new GetAllTransferState.Input());

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

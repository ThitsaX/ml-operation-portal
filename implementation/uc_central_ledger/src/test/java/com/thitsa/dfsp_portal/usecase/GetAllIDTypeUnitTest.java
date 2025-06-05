package com.thitsa.dfsp_portal.usecase;

import com.thitsa.dfsp_portal.datasource.persistence.CentralLedgerDbSettings;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetAllIDType;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerUseCaseConfiguration.class, CentralLedgerDbSettings.class})
public class GetAllIDTypeUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllIDTypeUnitTest.class);

    @Autowired
    private GetAllIDType getAllIDType;

    @Test
    public void test_getAllIDTypeSuccessfully() throws Exception {

        var output = this.getAllIDType.execute(new GetAllIDType.Input());

        if (output.getIdTypeDataList() != null && output.getIdTypeDataList().size() > 0) {
            for (var obj : output.getIdTypeDataList())
                LOG.info(obj.getPartyIdentifierTypeId() + " , " + obj.getName());
        } else {
            LOG.info("No record");
        }
    }

}

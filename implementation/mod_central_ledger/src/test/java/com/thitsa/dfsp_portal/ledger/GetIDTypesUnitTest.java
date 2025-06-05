package com.thitsa.dfsp_portal.ledger;

import com.thitsa.dfsp_portal.ledger.persistence.CentralLedgerDBSetting;
import com.thitsa.dfsp_portal.ledger.query.GetIDTypes;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerConfiguration.class, CentralLedgerReadDbConfiguration.class, CentralLedgerDBSetting.class})
public class GetIDTypesUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetIDTypesUnitTest.class);

    @Autowired
    private GetIDTypes getIDTypes;

    @Test
    public void test_getIDTypesSuccessfully() throws Exception {

        GetIDTypes.Output output = this.getIDTypes.execute(
                new GetIDTypes.Input());

        if (output.getIdTypeDataList() != null && output.getIdTypeDataList().size() > 0) {
            for (var obj : output.getIdTypeDataList())
                LOG.info(obj.getPartyIdentifierTypeId() + " , " +
                        obj.getName()
                );
        } else {
            LOG.info("No record");
        }

    }

}

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

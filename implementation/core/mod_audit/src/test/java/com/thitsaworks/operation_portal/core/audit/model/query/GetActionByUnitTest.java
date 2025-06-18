//package com.thitsaworks.operation_portal.dfsp_portal.audit.domain.query;
//
//import com.thitsaworks.operation_portal.dfsp_portal.audit.AuditConfiguration;
//import com.thitsaworks.operation_portal.dfsp_portal.audit.domain.persistence.MySqlDbSettings;
//import com.thitsaworks.component.common.identifier.UserId;
//import com.thitsaworks.operation_portal.dfsp_portal.audit.query.GetActionByQuery;
//import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//
//@ContextConfiguration(classes = {AuditConfiguration.class, MySqlDbSettings.class})
//public class GetActionByUnitTest extends EnvAwareUnitTest {
//
//    private static final Logger LOG = LoggerFactory.getLogger(GetActionByUnitTest.class);
//
//    @Autowired
//    private GetActionByQuery getActionBy;
//
//    @Test
//    public void test_getActionBySuccessfully() throws Exception {
//
//        GetActionByQuery.Output output = this.getActionBy.execute(new GetActionByQuery.Input(new UserId(392628367895068672L)));
//
//        LOG.info(output.getUserData().getUserId().toString() + "," + output.getUserData().getParticipantId());
//
//    }
//
//}

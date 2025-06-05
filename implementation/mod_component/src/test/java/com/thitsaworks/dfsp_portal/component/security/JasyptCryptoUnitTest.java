package com.thitsaworks.dfsp_portal.component.security;

import com.thitsaworks.dfsp_portal.component.ComponentConfiguration;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentConfiguration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JasyptCryptoUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(JasyptCryptoUnitTest.class);

    @Autowired
    private JasyptCrypto jasyptCrypto;

    @Test
    public void encrypt(){

        LOG.info("{}",this.jasyptCrypto.encrypt("hvs.twmiEOLjzdDYcGZdSeWzQhXP"));
    }
}

package com.thitsaworks.dfsp_portal.vault;

import com.thitsaworks.dfsp_portal.component.ComponentConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentConfiguration.class, VaultConfiguration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DfspValultUT {

    private static final Logger LOG = LoggerFactory.getLogger(DfspValultUT.class);

    @Autowired
    private DfspVault dfspVault;

    @Test
    public void test() {

        Settings settings =
                this.dfspVault.get("mysql/read_db/settings",
                        Settings.class);

        LOG.info("url :{}", settings.getUrl());
    }

    @NoArgsConstructor
    @Getter
    public static class Settings {

        String url;

        String username;

        String password;

        String showSql;

        String formatSql;

        int minPoolSize;

        int maxPoolSize;

    }

}

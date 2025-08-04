package com.thitsaworks.operation_portal.core.test_iam.IT.command;


import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


public class GetSnowflakeIdIT {

    @Test
    public void success() {
        for (int i = 0; i < 15; i++) {
            long id = Snowflake.get()
                               .nextId();
            System.out.println("Snowflake ID " + (i + 1) + ": " + id);
        }
    }


}

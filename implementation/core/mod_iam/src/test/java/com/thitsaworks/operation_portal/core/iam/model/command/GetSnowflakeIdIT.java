package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import org.junit.jupiter.api.Test;

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

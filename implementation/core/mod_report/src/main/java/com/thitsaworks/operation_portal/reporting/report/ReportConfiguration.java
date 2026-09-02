/*
 * Copyright (c) 2024-2026 ThitsaWorks Pte. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thitsaworks.operation_portal.reporting.report;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.hub.HubJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.hub.HubJpaPersistenceConfiguration;
import com.thitsaworks.operation_portal.core.reporting.download.ReportDownloadConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(
    value = {
        HubJdbcPersistenceConfiguration.class, HubJpaPersistenceConfiguration.class,
        CoreJdbcPersistenceConfiguration.class, ReportDownloadConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.reporting.report")
public class ReportConfiguration {

    @Bean
    public ReportConfiguration.Settings reportConfiguration() {

        return new ReportConfiguration.Settings(
            System.getProperty("RECEIVER_BIC"),
            System.getProperty("PACS_SENDER_GUIM_BIC"),
            System.getProperty("PACS_RECEIVER_BCRG_BIC"),
            System.getProperty("PACS_MEMBER_ID"));

    }

    public record Settings(String receiverBIC,
                           String senderGuiMBIC,
                           String receiverBCRGBIC,
                           String settlementMemberId) { }

}

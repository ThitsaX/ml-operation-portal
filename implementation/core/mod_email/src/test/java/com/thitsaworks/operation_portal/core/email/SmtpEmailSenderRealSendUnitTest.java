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
package com.thitsaworks.operation_portal.core.email;

import org.junit.jupiter.api.Test;

import java.util.Locale;

public class SmtpEmailSenderRealSendUnitTest {

    private static final String RECIPIENT_EMAIL = "";
    private static final String SMTP_HOST = "";
    private static final int SMTP_PORT = ;
    private static final String SENDER_NAME = "Operation Portal";
    private static final String SENDER_EMAIL = "";
    private static final boolean SMTP_AUTH = true;
    private static final boolean SMTP_STARTTLS_ENABLE = true;
    private static final boolean SMTP_SSL_ENABLE = false;
    private static final String EMAIL_SENDER_PASSWORD = "'";

    @Test
    public void test_sendRealNdcAlertMailWithEnvironmentTagSubjectSuccessfully() {

        EmailConfiguration.EmailSettings settings =
            new EmailConfiguration.EmailSettings(
                SMTP_HOST,
                SMTP_PORT,
                SENDER_NAME,
                SENDER_EMAIL,
                EMAIL_SENDER_PASSWORD,
                SMTP_AUTH,
                SMTP_STARTTLS_ENABLE,
                SMTP_SSL_ENABLE
            );

        EmailSender emailSender = new SmtpEmailSender(settings);

        emailSender.send(
            RECIPIENT_EMAIL,
            buildSubject("DemoDFSP1"),
            ndcThresholdAlertMessage()
        );
    }

    private static String buildSubject(String participantName) {

        String environment = System.getProperty("OPERATION_PORTAL_ENV", "LOCAL");
        String environmentTag =
            environment.isBlank() ? "LOCAL" : environment.trim().toUpperCase(Locale.ENGLISH);
        return "[ALERT][" + environmentTag + "][" + participantName + "] NDC Threshold Triggered";
    }

    private static String ndcThresholdAlertMessage() {

        return """
            <html>
            <body style="font-family: Arial, sans-serif; color: #30343b;">
            <table style="border-collapse: collapse; width: 100%; margin: 16px 0;">
                <thead>
                    <tr>
                        <th style="background-color: #f2f3f5; border: 1px solid #d9dde3; padding: 12px; text-align: left;">Date/Time</th>
                        <th style="background-color: #f2f3f5; border: 1px solid #d9dde3; padding: 12px; text-align: left;">Function</th>
                        <th style="background-color: #f2f3f5; border: 1px solid #d9dde3; padding: 12px; text-align: left;">Threshold Value</th>
                        <th style="background-color: #f2f3f5; border: 1px solid #d9dde3; padding: 12px; text-align: left;">Current Metric</th>
                        <th style="background-color: #f2f3f5; border: 1px solid #d9dde3; padding: 12px; text-align: left;">Currency</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="border: 1px solid #d9dde3; padding: 12px;">Sep 25, 2026 11:32:35 AM UTC</td>
                        <td style="border: 1px solid #d9dde3; padding: 12px;">NDC Threshold Triggered</td>
                        <td style="border: 1px solid #d9dde3; padding: 12px;">4%</td>
                        <td style="border: 1px solid #d9dde3; padding: 12px;">4.01%</td>
                        <td style="border: 1px solid #d9dde3; padding: 12px;">USD</td>
                    </tr>
                </tbody>
            </table>
            <p style="font-weight: 700;">&#9888; Immediate Action Required: Deposit Needed</p>
            <hr style="border: 0; border-top: 1px solid #d9dde3; margin: 16px 0;">
            <p>
            The information transmitted via this email is intended only for the addressee and may contain information
            that is privileged and confidential. If the reader of the message is not the intended recipient or an
            authorized representative thereof, be aware that any use or dissemination of this communication is strictly
            prohibited. If you received this in error, notify the sender and delete the material from your system.
            </p>
            </body>
            </html>
            """;
    }
}

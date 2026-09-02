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
package com.thitsaworks.operation_portal.reporting.report.command;

import com.thitsaworks.operation_portal.component.misc.security.xml.MxXadesXmlSigner;
import com.thitsaworks.operation_portal.component.misc.security.xml.MxXadesXmlVerifier;
import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import com.thitsaworks.operation_portal.reporting.report.domain.GeneratePacsFeeAmountSwiftReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.GeneratePacsTransactionAmountSwiftReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.impl.GeneratePacsFeeAmountSwiftReportCommandHandler;
import com.thitsaworks.operation_portal.reporting.report.domain.impl.GeneratePacsTransactionAmountSwiftReportCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.security.auth.x500.X500Principal;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static java.util.Map.entry;

@SuppressWarnings("deprecation")
public class GeneratePacsSwiftReportCommandHandlerUnitTest {

    private KeyPair keyPair;

    private TestCertificate certificate;

    private MxXadesXmlSigner signer;

    private MxXadesXmlVerifier verifier;

    @BeforeEach
    public void setUp() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.keyPair = keyPairGenerator.generateKeyPair();
        this.certificate = new TestCertificate(this.keyPair.getPublic());
        this.signer = new InMemoryMxXadesXmlSigner(this.keyPair.getPrivate(), this.certificate);
        this.verifier = new MxXadesXmlVerifier(signingSettings());
    }

    @Test
    public void generatePacsTransactionAmountSwiftReport_shouldReturnSignedRptByte() throws Exception {

        GeneratePacsTransactionAmountSwiftReportCommandHandler handler =
            new GeneratePacsTransactionAmountSwiftReportCommandHandler(
                new SingleRowJdbcTemplate(
                    Map.of(
                        "participantName", "payerfsp",
                        "participantBic", "PAYERBIC",
                        "participantAccountNumber", "PAYER001",
                        "settlementAgentBic", "PAYER001",
                        "isIndirectParticipant", false,
                        "currencyId", "MMK",
                        "amount", new BigDecimal("1250.50"),
                        "settlementDate", "260901",
                        "settlementCreationDate", "2026-09-01T09:00:00")),
                new ReportConfiguration.Settings("RECEIVERBIC"),
                this.signer);

        GeneratePacsTransactionAmountSwiftReportCommand.Output output = handler.execute(
            new GeneratePacsTransactionAmountSwiftReportCommand.Input("7", "MMK", "0630"));

        byte[] rptByte = output.feeSettlementRptByte();
        this.assertSignedRptByteIsValid(rptByte);

        Document document = this.parse(rptByte);
        var xpath = XPathFactory.newInstance().newXPath();
        assertEquals(
            "NimbaPayT/13",
            xpath.evaluate("//*[local-name()='SttlmCycl']/text()", document, XPathConstants.STRING));
        assertEquals(
            "1",
            xpath.evaluate("count(//*[local-name()='MvmntRcrd'])", document, XPathConstants.STRING));
        assertEquals(
            "1250.5",
            xpath.evaluate("//*[local-name()='CtrlSum']/text()", document, XPathConstants.STRING));
    }

    @Test
    public void generatePacsFeeAmountSwiftReport_shouldReturnSignedRptByte() throws Exception {

        GeneratePacsFeeAmountSwiftReportCommandHandler handler =
            new GeneratePacsFeeAmountSwiftReportCommandHandler(
                new SingleRowJdbcTemplate(
                    Map.ofEntries(
                        entry("payerDFSP", "payerfsp"),
                        entry("payerAccountNumber", "PAYER001"),
                        entry("payerSettlementAgentAccountNumber", "PAYER001"),
                        entry("payerIndirectParticipant", false),
                        entry("payeeDFSP", "payeefsp"),
                        entry("payeeAccountNumber", "PAYEE001"),
                        entry("payeeSettlementAgentAccountNumber", "PAYEE001"),
                        entry("payeeIndirectParticipant", false),
                        entry("hubAccountNumber", "HUB001"),
                        entry("hubSettlementAgentAccountNumber", "HUB001"),
                        entry("hubIndirectParticipant", false),
                        entry("currency", "MMK"),
                        entry("settlementDate", "260901"),
                        entry("settlementCreationDate", "2026-09-01T09:00:00"),
                        entry("payerFee", new BigDecimal("5.00")),
                        entry("hubFee", new BigDecimal("2.50")))),
                new ReportConfiguration.Settings("RECEIVERBIC"),
                this.signer);

        GeneratePacsFeeAmountSwiftReportCommand.Output output = handler.execute(
            new GeneratePacsFeeAmountSwiftReportCommand.Input("7", "MMK", "0630"));

        byte[] rptByte = output.feeSettlementRptByte();
        this.assertSignedRptByteIsValid(rptByte);

        Document document = this.parse(rptByte);
        var xpath = XPathFactory.newInstance().newXPath();
        assertEquals(
            "NimbaPayF/14",
            xpath.evaluate("//*[local-name()='SttlmCycl']/text()", document, XPathConstants.STRING));
        assertEquals(
            "3",
            xpath.evaluate("count(//*[local-name()='MvmntRcrd'])", document, XPathConstants.STRING));
        assertEquals(
            "15",
            xpath.evaluate("//*[local-name()='CtrlSum']/text()", document, XPathConstants.STRING));
    }

    private void assertSignedRptByteIsValid(byte[] rptByte) throws Exception {

        assertNotNull(rptByte);
        assertFalse(new String(rptByte, StandardCharsets.UTF_8).isBlank());
        this.verifier.verify(rptByte, this.certificate);

        Document document = this.parse(rptByte);
        var xpath = XPathFactory.newInstance().newXPath();
        assertEquals(
            "1",
            xpath.evaluate(
                "count(//*[local-name()='AppHdr']/*[local-name()='Sgntr']/*[local-name()='Signature'])",
                document,
                XPathConstants.STRING));
        assertEquals(
            "3",
            xpath.evaluate(
                "count(//*[local-name()='SignedInfo']/*[local-name()='Reference'])",
                document,
                XPathConstants.STRING));
        assertEquals(
            "1",
            xpath.evaluate(
                "count(//*[local-name()='KeyInfo']/*[local-name()='X509Data']/*[local-name()='X509IssuerSerial'])",
                document,
                XPathConstants.STRING));
    }

    private Document parse(byte[] xml) throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory
                   .newDocumentBuilder()
                   .parse(new InputSource(new ByteArrayInputStream(xml)));
    }

    private static MxXadesXmlSigner.Settings signingSettings() {

        return new MxXadesXmlSigner.Settings(
            true,
            "PKCS12",
            "/run/secrets/pacs029-signing.p12",
            "keystore-password",
            "operation-portal-signing",
            "key-password",
            true);
    }

    private static class InMemoryMxXadesXmlSigner extends MxXadesXmlSigner {

        private final PrivateKey privateKey;

        private final X509Certificate certificate;

        InMemoryMxXadesXmlSigner(PrivateKey privateKey, X509Certificate certificate) {

            super(signingSettings());
            this.privateKey = privateKey;
            this.certificate = certificate;
        }

        @Override
        public byte[] sign(byte[] unsignedXml) {

            return super.sign(unsignedXml, this.privateKey, this.certificate);
        }

    }

    private static class SingleRowJdbcTemplate extends JdbcTemplate {

        private final Map<String, Object> row;

        SingleRowJdbcTemplate(Map<String, Object> row) {

            this.row = row;
        }

        @Override
        public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {

            try {
                return List.of(rowMapper.mapRow(this.resultSet(), 0));
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to map fake JDBC row", exception);
            }
        }

        private ResultSet resultSet() {

            return (ResultSet) java.lang.reflect.Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class[] {ResultSet.class},
                (proxy, method, args) -> {
                    if (args == null || args.length != 1 || !(args[0] instanceof String columnName)) {
                        return this.defaultValue(method.getReturnType());
                    }

                    Object value = this.row.get(columnName);
                    return switch (method.getName()) {
                        case "getString" -> value == null ? null : value.toString();
                        case "getBigDecimal" -> value;
                        case "getBoolean" -> Boolean.TRUE.equals(value);
                        default -> this.defaultValue(method.getReturnType());
                    };
                });
        }

        private Object defaultValue(Class<?> returnType) {

            if (returnType.equals(boolean.class)) {
                return false;
            }
            if (returnType.equals(int.class)) {
                return 0;
            }
            if (returnType.equals(long.class)) {
                return 0L;
            }
            if (returnType.equals(double.class)) {
                return 0D;
            }
            return null;
        }

    }

    private static class TestCertificate extends X509Certificate {

        private final PublicKey publicKey;

        TestCertificate(PublicKey publicKey) {

            this.publicKey = publicKey;
        }

        @Override
        public X500Principal getIssuerX500Principal() {

            return new X500Principal("CN=Operation Portal Test CA");
        }

        @Override
        public BigInteger getSerialNumber() {

            return BigInteger.ONE;
        }

        @Override
        public PublicKey getPublicKey() {

            return this.publicKey;
        }

        @Override
        public void checkValidity() { }

        @Override
        public void checkValidity(Date date) { }

        @Override
        public int getVersion() {

            return 3;
        }

        @Override
        public Principal getIssuerDN() {

            return this.getIssuerX500Principal();
        }

        @Override
        public Principal getSubjectDN() {

            return new X500Principal("CN=Operation Portal");
        }

        @Override
        public Date getNotBefore() {

            return new Date(0);
        }

        @Override
        public Date getNotAfter() {

            return new Date(Long.MAX_VALUE);
        }

        @Override
        public byte[] getTBSCertificate() {

            return new byte[0];
        }

        @Override
        public byte[] getSignature() {

            return new byte[0];
        }

        @Override
        public String getSigAlgName() {

            return "SHA256withRSA";
        }

        @Override
        public String getSigAlgOID() {

            return "1.2.840.113549.1.1.11";
        }

        @Override
        public byte[] getSigAlgParams() {

            return new byte[0];
        }

        @Override
        public boolean[] getIssuerUniqueID() {

            return new boolean[0];
        }

        @Override
        public boolean[] getSubjectUniqueID() {

            return new boolean[0];
        }

        @Override
        public boolean[] getKeyUsage() {

            return new boolean[0];
        }

        @Override
        public int getBasicConstraints() {

            return -1;
        }

        @Override
        public byte[] getEncoded() {

            return new byte[0];
        }

        @Override
        public void verify(PublicKey key) { }

        @Override
        public void verify(PublicKey key, String sigProvider) { }

        @Override
        public String toString() {

            return "TestCertificate";
        }

        @Override
        public Set<String> getCriticalExtensionOIDs() {

            return Set.of();
        }

        @Override
        public Set<String> getNonCriticalExtensionOIDs() {

            return Set.of();
        }

        @Override
        public byte[] getExtensionValue(String oid) {

            return new byte[0];
        }

        @Override
        public boolean hasUnsupportedCriticalExtension() {

            return false;
        }

    }

}

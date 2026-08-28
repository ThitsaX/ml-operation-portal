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
package com.thitsaworks.operation_portal.component.security.xml;

import com.thitsaworks.operation_portal.component.misc.security.xml.MxXadesXmlSigner;
import com.thitsaworks.operation_portal.component.misc.security.xml.MxXadesXmlVerifier;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.security.auth.x500.X500Principal;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("deprecation")
public class MxXadesXmlSignerUnitTest {

    @Test
    public void sign_shouldAddXadesSignatureUnderAppHeaderSignatureNode() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        byte[] signedXml = new MxXadesXmlSigner(new MxXadesXmlSigner.Settings(
            true,
            "PKCS12",
            "/run/secrets/pacs029-signing.p12",
            "keystore-password",
            "operation-portal-signing",
            "key-password",
            true)).sign(
            sampleMxXml().getBytes(StandardCharsets.UTF_8),
            keyPair.getPrivate(),
            new TestCertificate(keyPair.getPublic()));

        new MxXadesXmlVerifier(new MxXadesXmlSigner.Settings(
            true,
            "PKCS12",
            "/run/secrets/pacs029-signing.p12",
            "keystore-password",
            "operation-portal-signing",
            "key-password",
            true)).verify(signedXml, new TestCertificate(keyPair.getPublic()));

        Document document = this.parse(signedXml);
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

    private static String sampleMxXml() {

        return """
            <DataPDU xmlns="urn:cma:stp:xsd:stp.1.0">
                <Body>
                    <AppHdr xmlns="urn:iso:std:iso:20022:tech:xsd:head.001.001.02">
                        <Fr>
                            <FIId>
                                <FinInstnId>
                                    <BICFI>REPCGNGA</BICFI>
                                </FinInstnId>
                            </FIId>
                        </Fr>
                        <To>
                            <FIId>
                                <FinInstnId>
                                    <BICFI>REPCGNGA</BICFI>
                                </FinInstnId>
                            </FIId>
                        </To>
                        <BizMsgIdr>GWRU60926073005</BizMsgIdr>
                        <MsgDefIdr>pacs.029.001.01</MsgDefIdr>
                        <BizSvc>swift.iap.tia.03</BizSvc>
                        <CreDt>2026-07-10T13:30:00+00:00</CreDt>
                    </AppHdr>
                    <Document xmlns="urn:iso:std:iso:20022:tech:xsd:pacs.029.001.01">
                        <MulSttlmReq>
                            <GrpHdr>
                                <MsgId>GWRU60926073005</MsgId>
                                <CreDtTm>2026-07-10T13:30:00+00:00</CreDtTm>
                                <NbOfSttlmReqs>1</NbOfSttlmReqs>
                                <CtrlSum>100</CtrlSum>
                            </GrpHdr>
                        </MulSttlmReq>
                    </Document>
                </Body>
            </DataPDU>
            """;
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

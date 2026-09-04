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
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("deprecation")
public class MxXadesXmlSignerUnitTest {

    @Test
    public void sign_shouldGenerateSignedRptByteFromReportSampleData() throws Exception {

        KeyPair keyPair = this.generateKeyPair();
        X509Certificate certificate = new TestCertificate(keyPair.getPublic());

        byte[] signedRptByte = this.generateSignedRptByte(keyPair, certificate);
        String signedRptByteBase64 = Base64.getEncoder().encodeToString(signedRptByte);

        this.logSignedRptByte(signedRptByte, signedRptByteBase64);

        assertFalse(signedRptByteBase64.isBlank());
        new MxXadesXmlVerifier(verifierSettings()).verify(signedRptByte, certificate);

        Document document = this.parse(signedRptByte);
        var xpath = XPathFactory.newInstance().newXPath();

        assertEquals(
            "NimbaPayT-260716-111",
            xpath.evaluate("//*[local-name()='BizMsgIdr']/text()", document, XPathConstants.STRING));
        assertEquals(
            "NimbaPayT/111",
            xpath.evaluate("//*[local-name()='SttlmCycl']/text()", document, XPathConstants.STRING));
        assertEquals(
            "6",
            xpath.evaluate("count(//*[local-name()='MvmntRcrd'])", document, XPathConstants.STRING));
        assertEquals(
            "2804000",
            xpath.evaluate("//*[local-name()='CtrlSum']/text()", document, XPathConstants.STRING));
        assertEquals(
            "2",
            xpath.evaluate("count(//*[local-name()='AnyBIC'][not(text())])", document, XPathConstants.STRING));
    }

    @Test
    public void sign_shouldAddXadesSignatureUnderAppHeaderSignatureNode() throws Exception {

        KeyPair keyPair = this.generateKeyPair();
        X509Certificate certificate = new TestCertificate(keyPair.getPublic());

        byte[] signedXml = this.generateSignedRptByte(keyPair, certificate);

        new MxXadesXmlVerifier(verifierSettings()).verify(signedXml, certificate);

        assertTrue(new String(signedXml, StandardCharsets.UTF_8).contains("<AnyBIC></AnyBIC>"));

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

    private KeyPair generateKeyPair() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private byte[] generateSignedRptByte(KeyPair keyPair,
                                         X509Certificate certificate) {

        return new MxXadesXmlSigner(signerSettings()).sign(
            sampleMxXml().getBytes(StandardCharsets.UTF_8),
            keyPair.getPrivate(),
            certificate);
    }

    private void logSignedRptByte(byte[] signedRptByte,
                                  String signedRptByteBase64) {

        System.out.println("signedRptByte XML:");
        System.out.println(new String(signedRptByte, StandardCharsets.UTF_8));
        System.out.println("signedRptByte Base64:");
        System.out.println(signedRptByteBase64);
    }

    private static MxXadesXmlSigner.Settings signerSettings() {

        return new MxXadesXmlSigner.Settings(
            true,
            "PKCS12",
            "/your/path/pacs029-signing.p12",
            "keystore-password",
            "operation-portal-signing",
            "key-password",
            true);
    }

    private static MxXadesXmlSigner.Settings verifierSettings() {

        return new MxXadesXmlSigner.Settings(
            true,
            "PKCS12",
            "/your/path/pacs029-signing.p12",
            "keystore-password",
            "operation-portal-signing",
            "key-password",
            true);
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
                        <BizMsgIdr>NimbaPayT-260716-111</BizMsgIdr>
                        <MsgDefIdr>pacs.029.001.01</MsgDefIdr>
                        <BizSvc>swift.iap.tia.03</BizSvc>
                        <CreDt>2026-07-16T09:50:04+06:30</CreDt>
                        <PssblDplct>false</PssblDplct>
                        <Prty>0009</Prty>
                    </AppHdr>
                    <Document xmlns="urn:iso:std:iso:20022:tech:xsd:pacs.029.001.01">
                        <MulSttlmReq>
                            <GrpHdr>
                                <MsgId>NimbaPayT-260716-111</MsgId>
                                <CreDtTm>2026-07-16T09:50:04+06:30</CreDtTm>
                                <NbOfSttlmReqs>6</NbOfSttlmReqs>
                                <CtrlSum>2804000</CtrlSum>
                                <SttlmInf>
                                    <SttlmMtd>CLRG</SttlmMtd>
                                    <ClrSys>
                                        <Cd>GNR</Cd>
                                    </ClrSys>
                                </SttlmInf>
                            </GrpHdr>
                            <SttlmReq>
                                <InstrId>RA</InstrId>
                                <SttlmCycl>NimbaPayT/111</SttlmCycl>
                                <NbOfMvmntRcrds>6</NbOfMvmntRcrds>
                                <MvmntRcrd>
                                    <Id>260716/111/1</Id>
                                    <SeqNb>1</SeqNb>
                                    <Amt>
                                        <Amt Ccy="GNF">1402000</Amt>
                                        <CdtDbt>DBIT</CdtDbt>
                                    </Amt>
                                    <SttlmAgt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC>111DemoDFSP1</AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </SttlmAgt>
                                    <Ptcpt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC>111DemoDFSP1</AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </Ptcpt>
                                    <Ref>012</Ref>
                                </MvmntRcrd>
                                <MvmntRcrd>
                                    <Id>260716/111/2</Id>
                                    <SeqNb>2</SeqNb>
                                    <Amt>
                                        <Amt Ccy="GNF">100000</Amt>
                                        <CdtDbt>CRDT</CdtDbt>
                                    </Amt>
                                    <SttlmAgt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC>210DFSP2</AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </SttlmAgt>
                                    <Ptcpt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC>210DFSP2</AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </Ptcpt>
                                    <Ref>012</Ref>
                                </MvmntRcrd>
                                <MvmntRcrd>
                                    <Id>260716/111/3</Id>
                                    <SeqNb>3</SeqNb>
                                    <Amt>
                                        <Amt Ccy="GNF">101000</Amt>
                                        <CdtDbt>CRDT</CdtDbt>
                                    </Amt>
                                    <SttlmAgt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC></AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </SttlmAgt>
                                    <Ptcpt>
                                        <Id>
                                            <OrgId>
                                                <Othr>
                                                    <Id>333cofina</Id>
                                                </Othr>
                                            </OrgId>
                                        </Id>
                                    </Ptcpt>
                                    <Ref>012</Ref>
                                </MvmntRcrd>
                                <MvmntRcrd>
                                    <Id>260716/111/4</Id>
                                    <SeqNb>4</SeqNb>
                                    <Amt>
                                        <Amt Ccy="GNF">201000</Amt>
                                        <CdtDbt>CRDT</CdtDbt>
                                    </Amt>
                                    <SttlmAgt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC>001bigbank</AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </SttlmAgt>
                                    <Ptcpt>
                                        <Id>
                                            <OrgId>
                                                <Othr>
                                                    <Id>222paycard</Id>
                                                </Othr>
                                            </OrgId>
                                        </Id>
                                    </Ptcpt>
                                    <Ref>012</Ref>
                                </MvmntRcrd>
                                <MvmntRcrd>
                                    <Id>260716/111/5</Id>
                                    <SeqNb>5</SeqNb>
                                    <Amt>
                                        <Amt Ccy="GNF">799000</Amt>
                                        <CdtDbt>CRDT</CdtDbt>
                                    </Amt>
                                    <SttlmAgt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC>001bigbank</AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </SttlmAgt>
                                    <Ptcpt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC>001bigbank</AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </Ptcpt>
                                    <Ref>012</Ref>
                                </MvmntRcrd>
                                <MvmntRcrd>
                                    <Id>260716/111/6</Id>
                                    <SeqNb>6</SeqNb>
                                    <Amt>
                                        <Amt Ccy="GNF">201000</Amt>
                                        <CdtDbt>CRDT</CdtDbt>
                                    </Amt>
                                    <SttlmAgt>
                                        <Id>
                                            <OrgId>
                                                <AnyBIC></AnyBIC>
                                            </OrgId>
                                        </Id>
                                    </SttlmAgt>
                                    <Ptcpt>
                                        <Id>
                                            <OrgId>
                                                <Othr>
                                                    <Id>45orange</Id>
                                                </Othr>
                                            </OrgId>
                                        </Id>
                                    </Ptcpt>
                                    <Ref>012</Ref>
                                </MvmntRcrd>
                            </SttlmReq>
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

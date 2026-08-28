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
package com.thitsaworks.operation_portal.component.misc.security.xml;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;

@Service
public class MxXadesXmlVerifier {

    private final MxXadesXmlSigner.Settings settings;

    private volatile X509Certificate signerCertificate;

    public MxXadesXmlVerifier(MxXadesXmlSigner.Settings settings) {

        this.settings = settings;
    }

    public void verify(byte[] signedXml) {

        this.verify(signedXml, this.getSignerCertificate());
    }

    public void verify(byte[] signedXml,
                       X509Certificate certificate) {

        try {
            this.settings.applyXmlSecuritySettings();
            Document document = this.parse(signedXml);
            NodeList signatureNodes = this.findSignatureNodes(document);

            for (int i = 0; i < signatureNodes.getLength(); i++) {
                this.verifySignature(document, (Element) signatureNodes.item(i), certificate);
            }
        } catch (MxXadesXmlVerificationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new MxXadesXmlVerificationException("Failed to verify MX XAdES XML signature", exception);
        }
    }

    private void verifySignature(Document document,
                                 Element signatureElement,
                                 X509Certificate certificate) throws Exception {

        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
        DOMValidateContext validateContext = new DOMValidateContext(
            new CertificateKeySelector(certificate), signatureElement);
        validateContext.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.TRUE);
        validateContext.setURIDereferencer(new MxDocumentUriDereferencer(this.findBusinessDocument(document)));

        Element signedProperties = this.findSignedProperties(signatureElement);
        validateContext.setIdAttributeNS(signedProperties, null, "Id");

        XMLSignature signature = signatureFactory.unmarshalXMLSignature(validateContext);
        this.validateReferences(signature, validateContext);

        if (!signature.validate(validateContext)) {
            throw new MxXadesXmlVerificationException("Invalid MX XAdES XML signature");
        }

        this.validateSigningTime(signedProperties, certificate);
    }

    private void validateReferences(XMLSignature signature,
                                    DOMValidateContext validateContext) throws Exception {

        Iterator<?> references = signature.getSignedInfo().getReferences().iterator();
        while (references.hasNext()) {
            Reference reference = (Reference) references.next();
            if (!reference.validate(validateContext)) {
                throw new MxXadesXmlVerificationException(
                    "Invalid MX XAdES XML signature reference: " + reference.getURI());
            }
        }
    }

    private void validateSigningTime(Element signedProperties,
                                     X509Certificate certificate) {

        NodeList signingTimeNodes = signedProperties.getElementsByTagNameNS(
            "http://uri.etsi.org/01903/v1.3.2#", "SigningTime");
        if (signingTimeNodes.getLength() == 0) {
            throw new MxXadesXmlVerificationException("SigningTime is missing in MX XAdES XML signature");
        }

        Date signingTime = Date.from(
            ZonedDateTime.parse(signingTimeNodes.item(0).getTextContent()).toInstant());
        if (signingTime.before(certificate.getNotBefore()) || signingTime.after(certificate.getNotAfter())) {
            throw new MxXadesXmlVerificationException(
                "SigningTime is outside signer certificate validity period");
        }
    }

    private NodeList findSignatureNodes(Document document) throws Exception {

        NodeList nodes = (NodeList) XPathFactory
                                       .newInstance()
                                       .newXPath()
                                       .evaluate(
                                           "//*[local-name()='Signature']",
                                           document.getDocumentElement(),
                                           XPathConstants.NODESET);
        if (nodes == null || nodes.getLength() == 0) {
            throw new MxXadesXmlVerificationException("Signature is missing in MX XML document");
        }
        return nodes;
    }

    private Element findBusinessDocument(Document document) {

        NodeList nodes = document.getElementsByTagNameNS("*", "Document");
        if (nodes.getLength() == 0) {
            throw new MxXadesXmlVerificationException("Mandatory element Document is missing");
        }
        return (Element) nodes.item(0);
    }

    private Element findSignedProperties(Element signatureElement) {

        NodeList nodes = signatureElement.getElementsByTagNameNS(
            "http://uri.etsi.org/01903/v1.3.2#", "SignedProperties");
        if (nodes.getLength() == 0) {
            throw new MxXadesXmlVerificationException("SignedProperties is missing in MX XAdES XML signature");
        }
        return (Element) nodes.item(0);
    }

    private X509Certificate getSignerCertificate() {

        X509Certificate current = this.signerCertificate;
        if (current != null) {
            return current;
        }

        synchronized (this) {
            if (this.signerCertificate == null) {
                this.signerCertificate = this.loadSignerCertificate();
            }
            return this.signerCertificate;
        }
    }

    private X509Certificate loadSignerCertificate() {

        if (!this.settings.enabled()) {
            throw new MxXadesXmlVerificationException("MX XAdES XML verification is disabled");
        }

        try {
            KeyStore keyStore = KeyStore.getInstance(this.settings.resolvedKeystoreType());
            try (var inputStream = Files.newInputStream(Path.of(this.settings.requiredKeystorePath()))) {
                keyStore.load(inputStream, this.settings.requiredKeystorePassword());
            }

            Certificate certificate = keyStore.getCertificate(this.settings.requiredKeyAlias());
            if (!(certificate instanceof X509Certificate signerCertificate)) {
                throw new MxXadesXmlVerificationException(
                    "Key alias does not reference an X509 certificate: " + this.settings.keyAlias());
            }
            return signerCertificate;
        } catch (MxXadesXmlVerificationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new MxXadesXmlVerificationException("Failed to load MX XAdES signer certificate", exception);
        }
    }

    private Document parse(byte[] xml) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        return factory
                   .newDocumentBuilder()
                   .parse(new InputSource(new ByteArrayInputStream(xml)));
    }

    private static class CertificateKeySelector extends KeySelector {

        private final X509Certificate certificate;

        CertificateKeySelector(X509Certificate certificate) {

            this.certificate = certificate;
        }

        @Override
        public KeySelectorResult select(KeyInfo keyInfo,
                                        Purpose purpose,
                                        AlgorithmMethod method,
                                        XMLCryptoContext context) throws KeySelectorException {

            return this.certificate::getPublicKey;
        }

    }

}

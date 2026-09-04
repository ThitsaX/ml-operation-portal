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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MxXadesXmlSigner {

    public static final String MX_XADES_SIGNING_SETTINGS_PATH = "xml-key-store/settings";

    private static final String XML_DSIG_NS = XMLSignature.XMLNS;

    private static final String XADES_NS = "http://uri.etsi.org/01903/v1.3.2#";

    private static final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";

    private static final String SIGNED_PROPERTIES_TYPE = XADES_NS + "SignedProperties";

    private static final String SIGNED_PROPERTIES_ID_SUFFIX = "-signedprops";

    private final Settings settings;

    private volatile SigningCredentials signingCredentials;

    public MxXadesXmlSigner(Settings settings) {

        this.settings = settings;
    }

    public byte[] sign(byte[] unsignedXml) {

        SigningCredentials signingCredentials = this.getSigningCredentials();
        return this.sign(
            unsignedXml, signingCredentials.privateKey(), signingCredentials.signerCertificate());
    }

    public byte[] sign(byte[] unsignedXml,
                       PrivateKey privateKey,
                       X509Certificate signerCertificate) {

        try {
            Document document = this.parse(unsignedXml);
            Document signedDocument = this.sign(document, privateKey, signerCertificate);
            return this.insertSignatureIntoOriginalXml(unsignedXml, signedDocument);
        } catch (Exception exception) {
            throw new MxXadesXmlSigningException("Failed to sign MX XML document", exception);
        }
    }

    public Document sign(Document document,
                         PrivateKey privateKey,
                         X509Certificate signerCertificate) {

        Objects.requireNonNull(document, "document is required");
        Objects.requireNonNull(privateKey, "privateKey is required");
        Objects.requireNonNull(signerCertificate, "signerCertificate is required");

        if (!this.settings.enabled()) {
            throw new MxXadesXmlSigningException("MX XAdES XML signing is disabled");
        }

        try {
            this.settings.applyXmlSecuritySettings();
            XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
            DigestMethod digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA256, null);
            CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(
                CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null);
            SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(RSA_SHA256, null);

            String keyInfoId = "_" + UUID.randomUUID();
            KeyInfo keyInfo = this.createKeyInfo(signatureFactory, signerCertificate, keyInfoId);

            String signatureId = "_" + UUID.randomUUID();
            String signedPropertiesId = "_" + UUID.randomUUID() + SIGNED_PROPERTIES_ID_SUFFIX;
            List<Reference> references = this.createReferences(
                signatureFactory, digestMethod, canonicalizationMethod, keyInfoId,
                signedPropertiesId);
            SignedInfo signedInfo = signatureFactory.newSignedInfo(
                canonicalizationMethod, signatureMethod, references);

            Node signatureNode = this.findOrCreateSignatureNode(document);
            DOMSignContext signContext = new DOMSignContext(privateKey, signatureNode);
            signContext.putNamespacePrefix(XML_DSIG_NS, "ds");
            signContext.setURIDereferencer(
                new MxDocumentUriDereferencer(this.findBusinessDocument(document)));

            XMLObject xadesObject = this.createXadesObject(
                document, signatureFactory, signContext, signatureId, signedPropertiesId);
            XMLSignature signature = signatureFactory.newXMLSignature(
                signedInfo, keyInfo, Collections.singletonList(xadesObject), signatureId, null);
            signature.sign(signContext);

            return document;
        } catch (Exception exception) {
            throw new MxXadesXmlSigningException("Failed to sign MX XML document", exception);
        }
    }

    private KeyInfo createKeyInfo(XMLSignatureFactory signatureFactory,
                                  X509Certificate signerCertificate,
                                  String keyInfoId) {

        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
        X509IssuerSerial issuerSerial = keyInfoFactory.newX509IssuerSerial(
            signerCertificate.getIssuerX500Principal().toString(),
            signerCertificate.getSerialNumber());
        X509Data x509Data = keyInfoFactory.newX509Data(Collections.singletonList(issuerSerial));
        return keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data), keyInfoId);
    }

    private List<Reference> createReferences(XMLSignatureFactory signatureFactory,
                                             DigestMethod digestMethod,
                                             CanonicalizationMethod canonicalizationMethod,
                                             String keyInfoId,
                                             String signedPropertiesId) throws Exception {

        List<Reference> references = new ArrayList<>();
        references.add(signatureFactory.newReference(
            "#" + keyInfoId, digestMethod,
            Collections.singletonList(canonicalizationMethod), null, null));
        references.add(signatureFactory.newReference(
            "#" + signedPropertiesId, digestMethod,
            Collections.singletonList(canonicalizationMethod), SIGNED_PROPERTIES_TYPE, null));
        references.add(signatureFactory.newReference(
            null, digestMethod,
            Collections.singletonList(canonicalizationMethod), null, null));
        return references;
    }

    private XMLObject createXadesObject(Document document,
                                        XMLSignatureFactory signatureFactory,
                                        DOMSignContext signContext,
                                        String signatureId,
                                        String signedPropertiesId) {

        Element qualifyingProperties = document.createElementNS(
            XADES_NS, "xades:QualifyingProperties");
        qualifyingProperties.setAttributeNS(
            XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:xades", XADES_NS);
        qualifyingProperties.setAttribute("Target", "#" + signatureId);

        Element signedProperties = document.createElementNS(XADES_NS, "xades:SignedProperties");
        signedProperties.setAttributeNS(
            XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:xades", XADES_NS);
        signedProperties.setAttribute("Id", signedPropertiesId);
        signedProperties.setIdAttribute("Id", true);
        signContext.setIdAttributeNS(signedProperties, null, "Id");
        qualifyingProperties.appendChild(signedProperties);

        Element signedSignatureProperties = document.createElementNS(
            XADES_NS, "xades:SignedSignatureProperties");
        signedProperties.appendChild(signedSignatureProperties);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Element signingTime = document.createElementNS(XADES_NS, "xades:SigningTime");
        signingTime.appendChild(document.createTextNode(dateFormat.format(new Date())));
        signedSignatureProperties.appendChild(signingTime);

        return signatureFactory.newXMLObject(
            Collections.singletonList(new DOMStructure(qualifyingProperties)), null, null, null);
    }

    private Node findOrCreateSignatureNode(Document document) {

        Element appHeader = this.findFirstElement(document, "AppHdr");
        Element signatureNode = null;
        Node relatedNode = null;

        NodeList children = appHeader.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if ("Sgntr".equals(child.getLocalName())) {
                    signatureNode = (Element) child;
                } else if ("Rltd".equals(child.getLocalName())) {
                    relatedNode = child;
                }
            }
        }

        if (signatureNode != null) {
            return signatureNode;
        }

        Element newSignatureNode = document.createElementNS(appHeader.getNamespaceURI(), "Sgntr");
        if (relatedNode == null) {
            return appHeader.appendChild(newSignatureNode);
        }
        return appHeader.insertBefore(newSignatureNode, relatedNode);
    }

    private Element findBusinessDocument(Document document) {

        return this.findFirstElement(document, "Document");
    }

    private Element findFirstElement(Document document, String localName) {

        NodeList nodes = document.getElementsByTagNameNS("*", localName);
        if (nodes.getLength() == 0) {
            throw new MxXadesXmlSigningException("Mandatory element " + localName + " is missing");
        }
        return (Element) nodes.item(0);
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

        return factory
                   .newDocumentBuilder()
                   .parse(
                       new InputSource(new StringReader(new String(xml, StandardCharsets.UTF_8))));
    }

    private byte[] serialize(Document document) throws Exception {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        var transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        return outputStream.toByteArray();
    }

    private byte[] insertSignatureIntoOriginalXml(byte[] unsignedXml, Document signedDocument) throws Exception {

        String originalXml = new String(unsignedXml, StandardCharsets.UTF_8);
        String signatureXml = this.serializeNode(this.findFirstElement(signedDocument, "Sgntr"));

        Pattern existingSignaturePattern = Pattern.compile(
            "<(?:[\\w.-]+:)?Sgntr\\b[^>]*/>|<(?:[\\w.-]+:)?Sgntr\\b[^>]*>.*?</(?:[\\w.-]+:)?Sgntr>",
            Pattern.DOTALL);
        Matcher existingSignatureMatcher = existingSignaturePattern.matcher(originalXml);
        if (existingSignatureMatcher.find()) {
            return existingSignatureMatcher
                       .replaceFirst(Matcher.quoteReplacement(signatureXml))
                       .getBytes(StandardCharsets.UTF_8);
        }

        Pattern appHeaderEndPattern = Pattern.compile("</(?:[\\w.-]+:)?AppHdr>");
        Matcher appHeaderEndMatcher = appHeaderEndPattern.matcher(originalXml);
        if (!appHeaderEndMatcher.find()) {
            return this.serialize(signedDocument);
        }

        String signedXml =
            originalXml.substring(0, appHeaderEndMatcher.start()) +
                signatureXml +
                originalXml.substring(appHeaderEndMatcher.start());
        return signedXml.getBytes(StandardCharsets.UTF_8);
    }

    private String serializeNode(Node node) throws Exception {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        var transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(node), new StreamResult(outputStream));
        return outputStream.toString(StandardCharsets.UTF_8);
    }

    private SigningCredentials getSigningCredentials() {

        SigningCredentials current = this.signingCredentials;
        if (current != null) {
            return current;
        }

        synchronized (this) {
            if (this.signingCredentials == null) {
                this.signingCredentials = this.loadSigningCredentials();
            }
            return this.signingCredentials;
        }
    }

    private SigningCredentials loadSigningCredentials() {

        if (!this.settings.enabled()) {
            throw new MxXadesXmlSigningException("MX XAdES XML signing is disabled");
        }

        try {
            KeyStore keyStore = KeyStore.getInstance(this.settings.resolvedKeystoreType());
            this.loadKeyStore(keyStore);

            Key key = keyStore.getKey(
                this.settings.requiredKeyAlias(), this.settings.resolvedKeyPassword());
            if (!(key instanceof PrivateKey privateKey)) {
                throw new MxXadesXmlSigningException(
                    "Key alias does not reference a private key: " + this.settings.keyAlias());
            }

            Certificate certificate = keyStore.getCertificate(this.settings.requiredKeyAlias());
            if (!(certificate instanceof X509Certificate signerCertificate)) {
                throw new MxXadesXmlSigningException(
                    "Key alias does not reference an X509 certificate: " +
                        this.settings.keyAlias());
            }

            return new SigningCredentials(privateKey, signerCertificate);
        } catch (MxXadesXmlSigningException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new MxXadesXmlSigningException(
                "Failed to load MX XAdES signing credentials", exception);
        }
    }

    private void loadKeyStore(KeyStore keyStore) throws Exception {

        Path keyStorePath = Path.of(this.settings.requiredKeystorePath());
        byte[] keyStoreBytes = Files.readAllBytes(keyStorePath);
        if (keyStorePath.getFileName().toString().endsWith(".b64")) {
            keyStoreBytes = Base64.getMimeDecoder().decode(new String(keyStoreBytes, StandardCharsets.UTF_8));
        }

        try (var inputStream = new ByteArrayInputStream(keyStoreBytes)) {
            keyStore.load(inputStream, this.settings.requiredKeystorePassword());
        }
    }

    public record Settings(boolean enabled,
                           String keystoreType,
                           String keystorePath,
                           String keystorePassword,
                           String keyAlias,
                           String keyPassword,
                           boolean ignoreLineBreaks) {

        public static Settings disabled() {

            return new Settings(false, "PKCS12", "", "", "", "", true);
        }

        public void applyXmlSecuritySettings() {

            String value = Boolean.toString(this.ignoreLineBreaks);
            System.setProperty("org.apache.xml.security.ignoreLineBreaks", value);
            System.setProperty("com.sun.org.apache.xml.internal.security.ignoreLineBreaks", value);
        }

        String resolvedKeystoreType() {

            return this.hasText(this.keystoreType) ? this.keystoreType : "PKCS12";
        }

        String requiredKeystorePath() {

            return this.requireText(this.keystorePath, "keystorePath");
        }

        char[] requiredKeystorePassword() {

            return this.requireText(this.keystorePassword, "keystorePassword").toCharArray();
        }

        String requiredKeyAlias() {

            return this.requireText(this.keyAlias, "keyAlias");
        }

        char[] resolvedKeyPassword() {

            if (this.hasText(this.keyPassword)) {
                return this.keyPassword.toCharArray();
            }
            return this.requiredKeystorePassword();
        }

        private String requireText(String value, String fieldName) {

            if (!this.hasText(value)) {
                throw new MxXadesXmlSigningException("MX XAdES setting is missing: " + fieldName);
            }
            return value;
        }

        private boolean hasText(String value) {

            return value != null && !value.isBlank();
        }

    }

    private record SigningCredentials(PrivateKey privateKey, X509Certificate signerCertificate) { }

}

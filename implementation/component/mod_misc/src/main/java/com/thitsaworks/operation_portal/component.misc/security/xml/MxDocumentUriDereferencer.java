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

import org.w3c.dom.Node;

import javax.xml.crypto.Data;
import javax.xml.crypto.NodeSetData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MxDocumentUriDereferencer implements URIDereferencer {

    private final Node businessDocument;

    MxDocumentUriDereferencer(Node businessDocument) {

        if (businessDocument == null) {
            throw new IllegalArgumentException("businessDocument is required");
        }
        this.businessDocument = businessDocument;
    }

    @Override
    public Data dereference(URIReference uriReference, XMLCryptoContext context)
        throws URIReferenceException {

        if (uriReference.getURI() == null || uriReference.getURI().isBlank()) {
            return new SubtreeNodeSetData(this.businessDocument);
        }

        URIDereferencer defaultDereferencer = XMLSignatureFactory
                                                  .getInstance("DOM")
                                                  .getURIDereferencer();
        return defaultDereferencer.dereference(uriReference, context);
    }

    private static class SubtreeNodeSetData implements NodeSetData {

        private final List<Node> nodes = new ArrayList<>();

        SubtreeNodeSetData(Node rootNode) {

            this.addSubtree(rootNode);
        }

        @Override
        public Iterator<Node> iterator() {

            return this.nodes.iterator();
        }

        private void addSubtree(Node node) {

            this.nodes.add(node);

            Node child = node.getFirstChild();
            while (child != null) {
                this.addSubtree(child);
                child = child.getNextSibling();
            }
        }

    }

}

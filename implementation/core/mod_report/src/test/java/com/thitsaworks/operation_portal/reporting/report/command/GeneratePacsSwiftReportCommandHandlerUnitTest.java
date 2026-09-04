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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation")
public class GeneratePacsSwiftReportCommandHandlerUnitTest {

    private MxXadesXmlVerifier verifier;

    @BeforeEach
    public void setUp() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.verifier = new MxXadesXmlVerifier(signingSettings());
    }

    @Test
    public void pastedRptByte_shouldBeValidSignedSwiftReport() throws Exception {

        String pastedRptByte = """
            PERhdGFQRFUgeG1sbnM9InVybjpjbWE6c3RwOnhzZDpzdHAuMS4wIj4KICA8Qm9keT4KICAgIDxB
            cHBIZHIgeG1sbnM9InVybjppc286c3RkOmlzbzoyMDAyMjp0ZWNoOnhzZDpoZWFkLjAwMS4wMDEu
            MDIiPgogICAgICA8RnI+CiAgICAgICAgPEZJSWQ+CiAgICAgICAgICA8RmluSW5zdG5JZD4KICAg
            ICAgICAgICAgPEJJQ0ZJPlJFUENHTkdBPC9CSUNGST4KICAgICAgICAgICAgPENsclN5c01tYklk
            PgogICAgICAgICAgICAgIDxDbHJTeXNJZD4KICAgICAgICAgICAgICAgIDxDZD5HSU5QQTwvQ2Q+
            CiAgICAgICAgICAgICAgPC9DbHJTeXNJZD4KICAgICAgICAgICAgICA8TW1iSWQ+UkVQQ0dOR0FB
            U008L01tYklkPgogICAgICAgICAgICA8L0NsclN5c01tYklkPgogICAgICAgICAgPC9GaW5JbnN0
            bklkPgogICAgICAgIDwvRklJZD4KICAgICAgPC9Gcj4KICAgICAgPFRvPgogICAgICAgIDxGSUlk
            PgogICAgICAgICAgPEZpbkluc3RuSWQ+CiAgICAgICAgICAgIDxCSUNGST5SRVBDR05HQTwvQklD
            Rkk+CiAgICAgICAgICA8L0Zpbkluc3RuSWQ+CiAgICAgICAgPC9GSUlkPgogICAgICA8L1RvPgog
            ICAgICA8Qml6TXNnSWRyPk5pbWJhUGF5VC0yNjA3MTYtMTExPC9CaXpNc2dJZHI+CiAgICAgIDxN
            c2dEZWZJZHI+cGFjcy4wMjkuMDAxLjAxPC9Nc2dEZWZJZHI+CiAgICAgIDxCaXpTdmM+c3dpZnQu
            aWFwLnRpYS4wMzwvQml6U3ZjPgogICAgICA8Q3JlRHQ+MjAyNi0wNy0xNlQwOTo1MDowNCswNjoz
            MDwvQ3JlRHQ+CiAgICAgIDxQc3NibERwbGN0PmZhbHNlPC9Qc3NibERwbGN0PgogICAgICA8UHJ0
            eT4wMDA5PC9QcnR5PgogICAgPFNnbnRyIHhtbG5zPSJ1cm46aXNvOnN0ZDppc286MjAwMjI6dGVj
            aDp4c2Q6aGVhZC4wMDEuMDAxLjAyIj48ZHM6U2lnbmF0dXJlIHhtbG5zOmRzPSJodHRwOi8vd3d3
            LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIiBJZD0iXzQzZmJkNTBkLTA2NjEtNDhkZS05OWNkLTcy
            YzYxMzUzZDI1ZSI+PGRzOlNpZ25lZEluZm8+PGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxn
            b3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz48ZHM6U2ln
            bmF0dXJlTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxkc2ln
            LW1vcmUjcnNhLXNoYTI1NiIvPjxkczpSZWZlcmVuY2UgVVJJPSIjX2EyMTg1YjIyLTllODUtNGE5
            Ny1hMDBlLWVhODU2YWIzZDhkYSI+PGRzOlRyYW5zZm9ybXM+PGRzOlRyYW5zZm9ybSBBbGdvcml0
            aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPjwvZHM6VHJhbnNm
            b3Jtcz48ZHM6RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8w
            NC94bWxlbmMjc2hhMjU2Ii8+PGRzOkRpZ2VzdFZhbHVlPld4TlRPV2xLMTRPdWtTdDRha2xCdWJv
            OVY3SlZsYm5rYXphL0prN3hObXM9PC9kczpEaWdlc3RWYWx1ZT48L2RzOlJlZmVyZW5jZT48ZHM6
            UmVmZXJlbmNlIFR5cGU9Imh0dHA6Ly91cmkuZXRzaS5vcmcvMDE5MDMvdjEuMy4yI1NpZ25lZFBy
            b3BlcnRpZXMiIFVSST0iI19hNDFhZjZiNy02MDY1LTRlOTUtYWVhMC1hMjMzZTJhYjk3Njktc2ln
            bmVkcHJvcHMiPjxkczpUcmFuc2Zvcm1zPjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8v
            d3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz48L2RzOlRyYW5zZm9ybXM+PGRzOkRp
            Z2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3No
            YTI1NiIvPjxkczpEaWdlc3RWYWx1ZT5XUEVKZkVqckk0dSsyZ3RmcUNibFQxazArUjhSc3BHTUkx
            K2ZxR2pWYlEwPTwvZHM6RGlnZXN0VmFsdWU+PC9kczpSZWZlcmVuY2U+PGRzOlJlZmVyZW5jZT48
            ZHM6VHJhbnNmb3Jtcz48ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcv
            MjAwMS8xMC94bWwtZXhjLWMxNG4jIi8+PC9kczpUcmFuc2Zvcm1zPjxkczpEaWdlc3RNZXRob2Qg
            QWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGEyNTYiLz48ZHM6
            RGlnZXN0VmFsdWU+a2dmcEczclZxMHFBbUs2N29rTUNtc0FYR1ViaXNwZTZQNjAxYjFhZVYvYz08
            L2RzOkRpZ2VzdFZhbHVlPjwvZHM6UmVmZXJlbmNlPjwvZHM6U2lnbmVkSW5mbz48ZHM6U2lnbmF0
            dXJlVmFsdWU+cVd1Z29wWUNsS0p4VzhRYTdUYVBsMlJMR3d1MWk4L0hVcC8vb05xL0N3RTk5NXRB
            V052alU5emZXeW5CZm5PVzFPWHVMTUxpZStwbW1mTnZ5NUpwZllIMmVvUUhjSytNMEorREY2MkFG
            dGUwRGFIQ3ZQek5wSTQvbFJjek1TSWp0T2VzalJqeGJxY1lvRERMVzZwaGNGMGllankzeW5BZWdB
            OVBCKzh3SHlGeXRvc1NPR2IycTRFSkZjNDBwUzVtdGtjTTVYYjdxc3hxWkxFN2p2U0J4LzJVNG9N
            YWtvZkJndHhjWlFJK1ByNjZkSDdlM3R3SU9XSWFUTE5NeTJ0ZUQwd3VwWEVRa055enlwaHM3V3Fn
            VGhheGcwRnphZ2RBZ1VhcCtWRHpMdVh1VTdyUHRXRjRPYU1wTlloaTBhNHovTjFrVU5tZGRuZkFa
            REVma3FHT0NnPT08L2RzOlNpZ25hdHVyZVZhbHVlPjxkczpLZXlJbmZvIElkPSJfYTIxODViMjIt
            OWU4NS00YTk3LWEwMGUtZWE4NTZhYjNkOGRhIj48ZHM6WDUwOURhdGE+PGRzOlg1MDlJc3N1ZXJT
            ZXJpYWw+PGRzOlg1MDlJc3N1ZXJOYW1lPkNOPVRoaXRzYVdvcmtzLCBPPVRoaXRzYVdvcmtzLCBM
            PVNHLCBTVD1TRywgQz1TRzwvZHM6WDUwOUlzc3Vlck5hbWU+PGRzOlg1MDlTZXJpYWxOdW1iZXI+
            MjUzNTYwMzI0NDY2NzgyMjExMDA2MTUwMjEyMTA3OTQ4MTM1NTY0ODQ2NzUzOTA0PC9kczpYNTA5
            U2VyaWFsTnVtYmVyPjwvZHM6WDUwOUlzc3VlclNlcmlhbD48L2RzOlg1MDlEYXRhPjwvZHM6S2V5
            SW5mbz48ZHM6T2JqZWN0Pjx4YWRlczpRdWFsaWZ5aW5nUHJvcGVydGllcyB4bWxuczp4YWRlcz0i
            aHR0cDovL3VyaS5ldHNpLm9yZy8wMTkwMy92MS4zLjIjIiBUYXJnZXQ9IiNfNDNmYmQ1MGQtMDY2
            MS00OGRlLTk5Y2QtNzJjNjEzNTNkMjVlIj48eGFkZXM6U2lnbmVkUHJvcGVydGllcyBJZD0iX2E0
            MWFmNmI3LTYwNjUtNGU5NS1hZWEwLWEyMzNlMmFiOTc2OS1zaWduZWRwcm9wcyI+PHhhZGVzOlNp
            Z25lZFNpZ25hdHVyZVByb3BlcnRpZXM+PHhhZGVzOlNpZ25pbmdUaW1lPjIwMjYtMDktMDRUMTA6
            MzI6NTcrMDY6MzA8L3hhZGVzOlNpZ25pbmdUaW1lPjwveGFkZXM6U2lnbmVkU2lnbmF0dXJlUHJv
            cGVydGllcz48L3hhZGVzOlNpZ25lZFByb3BlcnRpZXM+PC94YWRlczpRdWFsaWZ5aW5nUHJvcGVy
            dGllcz48L2RzOk9iamVjdD48L2RzOlNpZ25hdHVyZT48L1NnbnRyPjwvQXBwSGRyPgogICAgPERv
            Y3VtZW50IHhtbG5zPSJ1cm46aXNvOnN0ZDppc286MjAwMjI6dGVjaDp4c2Q6cGFjcy4wMjkuMDAx
            LjAxIj4KICAgICAgPE11bFN0dGxtUmVxPgogICAgICAgIDxHcnBIZHI+CiAgICAgICAgICA8TXNn
            SWQ+TmltYmFQYXlULTI2MDcxNi0xMTE8L01zZ0lkPgogICAgICAgICAgPENyZUR0VG0+MjAyNi0w
            Ny0xNlQwOTo1MDowNCswNjozMDwvQ3JlRHRUbT4KICAgICAgICAgIDxOYk9mU3R0bG1SZXFzPjY8
            L05iT2ZTdHRsbVJlcXM+CiAgICAgICAgICA8Q3RybFN1bT4yODA0MDAwPC9DdHJsU3VtPgogICAg
            ICAgICAgPFN0dGxtSW5mPgogICAgICAgICAgICA8U3R0bG1NdGQ+Q0xSRzwvU3R0bG1NdGQ+CiAg
            ICAgICAgICAgIDxDbHJTeXM+CiAgICAgICAgICAgICAgPENkPkdOUjwvQ2Q+CiAgICAgICAgICAg
            IDwvQ2xyU3lzPgogICAgICAgICAgPC9TdHRsbUluZj4KICAgICAgICA8L0dycEhkcj4KICAgICAg
            ICA8U3R0bG1SZXE+CiAgICAgICAgICA8SW5zdHJJZD5SQTwvSW5zdHJJZD4KICAgICAgICAgIDxT
            dHRsbUN5Y2w+TmltYmFQYXlULzExMTwvU3R0bG1DeWNsPgogICAgICAgICAgPE5iT2ZNdm1udFJj
            cmRzPjY8L05iT2ZNdm1udFJjcmRzPgogICAgICAgICAgPE12bW50UmNyZD4KICAgICAgICAgICAg
            PElkPjI2MDcxNi8xMTEvMTwvSWQ+CiAgICAgICAgICAgIDxTZXFOYj4xPC9TZXFOYj4KICAgICAg
            ICAgICAgPEFtdD4KICAgICAgICAgICAgICA8QW10IENjeT0iR05GIj4xNDAyMDAwPC9BbXQ+CiAg
            ICAgICAgICAgICAgPENkdERidD5EQklUPC9DZHREYnQ+CiAgICAgICAgICAgIDwvQW10PgogICAg
            ICAgICAgICA8U3R0bG1BZ3Q+CiAgICAgICAgICAgICAgPElkPgogICAgICAgICAgICAgICAgPE9y
            Z0lkPgogICAgICAgICAgICAgICAgICA8QW55QklDPjExMURlbW9ERlNQMTwvQW55QklDPgogICAg
            ICAgICAgICAgICAgPC9PcmdJZD4KICAgICAgICAgICAgICA8L0lkPgogICAgICAgICAgICA8L1N0
            dGxtQWd0Pgo8UHRjcHQ+CiAgPElkPgogICAgPE9yZ0lkPgogICAgICA8QW55QklDPjExMURlbW9E
            RlNQMTwvQW55QklDPgogICAgPC9PcmdJZD4KICA8L0lkPgo8L1B0Y3B0PgogICAgICAgICAgICA8
            UmVmPjAxMjwvUmVmPgogICAgICAgICAgPC9Ndm1udFJjcmQ+CgogICAgICAgICAgPE12bW50UmNy
            ZD4KICAgICAgICAgICAgPElkPjI2MDcxNi8xMTEvMjwvSWQ+CiAgICAgICAgICAgIDxTZXFOYj4y
            PC9TZXFOYj4KICAgICAgICAgICAgPEFtdD4KICAgICAgICAgICAgICA8QW10IENjeT0iR05GIj4y
            MDAwMDA8L0FtdD4KICAgICAgICAgICAgICA8Q2R0RGJ0PkNSRFQ8L0NkdERidD4KICAgICAgICAg
            ICAgPC9BbXQ+CiAgICAgICAgICAgIDxTdHRsbUFndD4KICAgICAgICAgICAgICA8SWQ+CiAgICAg
            ICAgICAgICAgICA8T3JnSWQ+CiAgICAgICAgICAgICAgICAgIDxBbnlCSUM+MjEwREZTUDI8L0Fu
            eUJJQz4KICAgICAgICAgICAgICAgIDwvT3JnSWQ+CiAgICAgICAgICAgICAgPC9JZD4KICAgICAg
            ICAgICAgPC9TdHRsbUFndD4KPFB0Y3B0PgogIDxJZD4KICAgIDxPcmdJZD4KICAgICAgPEFueUJJ
            Qz4yMTBERlNQMjwvQW55QklDPgogICAgPC9PcmdJZD4KICA8L0lkPgo8L1B0Y3B0PgogICAgICAg
            ICAgICA8UmVmPjAxMjwvUmVmPgogICAgICAgICAgPC9Ndm1udFJjcmQ+CgogICAgICAgICAgPE12
            bW50UmNyZD4KICAgICAgICAgICAgPElkPjI2MDcxNi8xMTEvMzwvSWQ+CiAgICAgICAgICAgIDxT
            ZXFOYj4zPC9TZXFOYj4KICAgICAgICAgICAgPEFtdD4KICAgICAgICAgICAgICA8QW10IENjeT0i
            R05GIj4xMDEwMDA8L0FtdD4KICAgICAgICAgICAgICA8Q2R0RGJ0PkNSRFQ8L0NkdERidD4KICAg
            ICAgICAgICAgPC9BbXQ+CiAgICAgICAgICAgIDxTdHRsbUFndD4KICAgICAgICAgICAgICA8SWQ+
            CiAgICAgICAgICAgICAgICA8T3JnSWQ+CiAgICAgICAgICAgICAgICAgIDxBbnlCSUM+PC9BbnlC
            SUM+CiAgICAgICAgICAgICAgICA8L09yZ0lkPgogICAgICAgICAgICAgIDwvSWQ+CiAgICAgICAg
            ICAgIDwvU3R0bG1BZ3Q+CjxQdGNwdD4KICA8SWQ+CiAgICA8T3JnSWQ+CiAgICAgIDxPdGhyPgog
            ICAgICAgIDxJZD4zMzNjb2ZpbmE8L0lkPgogICAgICA8L090aHI+CiAgICA8L09yZ0lkPgogIDwv
            SWQ+CjwvUHRjcHQ+CiAgICAgICAgICAgIDxSZWY+MDEyPC9SZWY+CiAgICAgICAgICA8L012bW50
            UmNyZD4KCiAgICAgICAgICA8TXZtbnRSY3JkPgogICAgICAgICAgICA8SWQ+MjYwNzE2LzExMS80
            PC9JZD4KICAgICAgICAgICAgPFNlcU5iPjQ8L1NlcU5iPgogICAgICAgICAgICA8QW10PgogICAg
            ICAgICAgICAgIDxBbXQgQ2N5PSJHTkYiPjIwMTAwMDwvQW10PgogICAgICAgICAgICAgIDxDZHRE
            YnQ+Q1JEVDwvQ2R0RGJ0PgogICAgICAgICAgICA8L0FtdD4KICAgICAgICAgICAgPFN0dGxtQWd0
            PgogICAgICAgICAgICAgIDxJZD4KICAgICAgICAgICAgICAgIDxPcmdJZD4KICAgICAgICAgICAg
            ICAgICAgPEFueUJJQz4wMDFiaWdiYW5rPC9BbnlCSUM+CiAgICAgICAgICAgICAgICA8L09yZ0lk
            PgogICAgICAgICAgICAgIDwvSWQ+CiAgICAgICAgICAgIDwvU3R0bG1BZ3Q+CjxQdGNwdD4KICA8
            SWQ+CiAgICA8T3JnSWQ+CiAgICAgIDxPdGhyPgogICAgICAgIDxJZD4yMjJwYXljYXJkPC9JZD4K
            ICAgICAgPC9PdGhyPgogICAgPC9PcmdJZD4KICA8L0lkPgo8L1B0Y3B0PgogICAgICAgICAgICA8
            UmVmPjAxMjwvUmVmPgogICAgICAgICAgPC9Ndm1udFJjcmQ+CgogICAgICAgICAgPE12bW50UmNy
            ZD4KICAgICAgICAgICAgPElkPjI2MDcxNi8xMTEvNTwvSWQ+CiAgICAgICAgICAgIDxTZXFOYj41
            PC9TZXFOYj4KICAgICAgICAgICAgPEFtdD4KICAgICAgICAgICAgICA8QW10IENjeT0iR05GIj43
            OTkwMDA8L0FtdD4KICAgICAgICAgICAgICA8Q2R0RGJ0PkNSRFQ8L0NkdERidD4KICAgICAgICAg
            ICAgPC9BbXQ+CiAgICAgICAgICAgIDxTdHRsbUFndD4KICAgICAgICAgICAgICA8SWQ+CiAgICAg
            ICAgICAgICAgICA8T3JnSWQ+CiAgICAgICAgICAgICAgICAgIDxBbnlCSUM+MDAxYmlnYmFuazwv
            QW55QklDPgogICAgICAgICAgICAgICAgPC9PcmdJZD4KICAgICAgICAgICAgICA8L0lkPgogICAg
            ICAgICAgICA8L1N0dGxtQWd0Pgo8UHRjcHQ+CiAgPElkPgogICAgPE9yZ0lkPgogICAgICA8QW55
            QklDPjAwMWJpZ2Jhbms8L0FueUJJQz4KICAgIDwvT3JnSWQ+CiAgPC9JZD4KPC9QdGNwdD4KICAg
            ICAgICAgICAgPFJlZj4wMTI8L1JlZj4KICAgICAgICAgIDwvTXZtbnRSY3JkPgoKICAgICAgICAg
            IDxNdm1udFJjcmQ+CiAgICAgICAgICAgIDxJZD4yNjA3MTYvMTExLzY8L0lkPgogICAgICAgICAg
            ICA8U2VxTmI+NjwvU2VxTmI+CiAgICAgICAgICAgIDxBbXQ+CiAgICAgICAgICAgICAgPEFtdCBD
            Y3k9IkdORiI+MjAxMDAwPC9BbXQ+CiAgICAgICAgICAgICAgPENkdERidD5DUkRUPC9DZHREYnQ+
            CiAgICAgICAgICAgIDwvQW10PgogICAgICAgICAgICA8U3R0bG1BZ3Q+CiAgICAgICAgICAgICAg
            PElkPgogICAgICAgICAgICAgICAgPE9yZ0lkPgogICAgICAgICAgICAgICAgICA8QW55QklDPjwv
            QW55QklDPgogICAgICAgICAgICAgICAgPC9PcmdJZD4KICAgICAgICAgICAgICA8L0lkPgogICAg
            ICAgICAgICA8L1N0dGxtQWd0Pgo8UHRjcHQ+CiAgPElkPgogICAgPE9yZ0lkPgogICAgICA8T3Ro
            cj4KICAgICAgICA8SWQ+NDVvcmFuZ2U8L0lkPgogICAgICA8L090aHI+CiAgICA8L09yZ0lkPgog
            IDwvSWQ+CjwvUHRjcHQ+CiAgICAgICAgICAgIDxSZWY+MDEyPC9SZWY+CiAgICAgICAgICA8L012
            bW50UmNyZD4KCiAgICAgICAgPC9TdHRsbVJlcT4KICAgICAgPC9NdWxTdHRsbVJlcT4KICAgIDwv
            RG9jdW1lbnQ+CiAgPC9Cb2R5Pgo8L0RhdGFQRFU+Cg==
            """;

        byte[] rptByte = this.toRptByte(pastedRptByte);
        X509Certificate signerCertificate = this.loadCertificateFromTrustStore(
            Path.of("YOUR PATH"),
            "Password".toCharArray(), "");

        this.assertSignedRptByteStructureIsValid(rptByte);
        this.assertSignedRptByteSignatureIsValid(rptByte, signerCertificate);
    }

    private void assertSignedRptByteSignatureIsValid(byte[] rptByte,
                                                     X509Certificate signerCertificate) {

        this.verifier.verify(rptByte, signerCertificate);
    }

    private void assertSignedRptByteStructureIsValid(byte[] rptByte) throws Exception {

        assertNotNull(rptByte);
        String rptXml = new String(rptByte, StandardCharsets.UTF_8);
        assertFalse(rptXml.isBlank());
        assertFalse(rptXml.contains("PASTE_SIGNED_RPT_XML_HERE"));
        assertTrue(rptXml.contains("<AppHdr"));

        Document document = this.parse(rptByte);
        var xpath = XPathFactory.newInstance().newXPath();
        assertEquals(
            "1", xpath.evaluate(
                "count(//*[local-name()='AppHdr']/*[local-name()='Sgntr']/*[local-name()='Signature'])",
                document, XPathConstants.STRING));
        assertEquals(
            "3", xpath.evaluate(
                "count(//*[local-name()='SignedInfo']/*[local-name()='Reference'])", document,
                XPathConstants.STRING));
        assertEquals(
            "1", xpath.evaluate(
                "count(//*[local-name()='KeyInfo']/*[local-name()='X509Data']/*[local-name()='X509IssuerSerial'])",
                document, XPathConstants.STRING));
    }

    private byte[] toRptByte(String pastedRptByte) {

        String trimmedRptByte = pastedRptByte.strip();
        assertFalse(trimmedRptByte.isBlank());

        if (trimmedRptByte.startsWith("<")) {
            return trimmedRptByte.getBytes(StandardCharsets.UTF_8);
        }

        return Base64.getMimeDecoder().decode(trimmedRptByte);
    }

    private X509Certificate loadCertificateFromTrustStore(Path trustStorePath,
                                                          char[] trustStorePassword,
                                                          String certificateAlias)
        throws Exception {

        assertTrue(Files.exists(trustStorePath));
        assertFalse(new String(trustStorePassword).isBlank());

        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (var inputStream = Files.newInputStream(trustStorePath)) {
            trustStore.load(inputStream, trustStorePassword);
        }

        Certificate certificate = null;
        if (certificateAlias == null || certificateAlias.isBlank()) {
            Enumeration<String> aliases = trustStore.aliases();
            while (aliases.hasMoreElements() && !(certificate instanceof X509Certificate)) {
                certificate = trustStore.getCertificate(aliases.nextElement());
            }
        } else {
            certificate = trustStore.getCertificate(certificateAlias);
        }

        assertTrue(certificate instanceof X509Certificate);
        return (X509Certificate) certificate;
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
            true, "PKCS12", "YOUR PATH", "PASSWORD",
            "op_tw", "PASSWORD", true);
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
                ResultSet.class.getClassLoader(), new Class[]{ResultSet.class},
                (proxy, method, args) -> {
                    if (args == null || args.length != 1 ||
                            !(args[0] instanceof String columnName)) {
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

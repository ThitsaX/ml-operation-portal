package com.thitsaworks.dfsp_portal.component.http;

import lombok.Getter;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    @Getter
    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {

        super(request);

        InputStream requestInputStream = request.getInputStream();

        if (requestInputStream == null) {

            return;

        }

        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        return new CachedBodyServletInputStream(this.cachedBody);

    }

    @Override
    public BufferedReader getReader() throws IOException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);

        return new BufferedReader(new InputStreamReader(byteArrayInputStream));

    }

}
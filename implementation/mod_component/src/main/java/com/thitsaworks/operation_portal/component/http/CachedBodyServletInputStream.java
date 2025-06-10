package com.thitsaworks.operation_portal.component.http;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedBodyServletInputStream extends ServletInputStream {

    private static final Logger LOG = LoggerFactory.getLogger(CachedBodyServletInputStream.class);

    private InputStream cachedBodyInputStream;

    public CachedBodyServletInputStream(byte[] cachedBody) {

        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);

    }

    @Override
    public boolean isFinished() {

        try {

            return cachedBodyInputStream.available() == 0;

        } catch (IOException e) {

            LOG.error("Error : ", e);

        }

        return false;

    }

    @Override
    public boolean isReady() {

        return true;

    }

    @Override
    public int read() throws IOException {

        return cachedBodyInputStream.read();

    }

    @Override
    public void setReadListener(ReadListener readListener) {

        throw new UnsupportedOperationException();

    }

}
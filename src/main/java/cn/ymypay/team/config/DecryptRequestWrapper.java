package cn.ymypay.team.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

public class DecryptRequestWrapper extends HttpServletRequestWrapper {
    private final String decryptedBody;
    public DecryptRequestWrapper(HttpServletRequest request, String decryptedBody) {
        super(request);
        this.decryptedBody = decryptedBody;
    }
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new StringReader(decryptedBody));
    }
    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(decryptedBody.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteStream.available() == 0;
            }
            @Override
            public boolean isReady() {
                return true;
            }
            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }
            public int read() {
                return byteStream.read();
            }
        };
    }
}

package org.apache.cmueller.camel.esbperf.karaf.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EchoService extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (InputStream is = request.getInputStream(); OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];

            int n = 0;
            while (-1 != (n = is.read(buffer))) {
                os.write(buffer, 0, n);
            }

            os.flush();
        } finally {
            // nothing to do
        }
    }
}
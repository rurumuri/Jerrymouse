package com.rurumuri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.rurumuri.HttpServer.WEB_ROOT;

public class HttpResponse {

    private String requestUri;
    private OutputStream out;

    public HttpResponse(OutputStream outputStream) {
        out = outputStream;
    }

    public void setRequest(HttpRequest request) {
        requestUri = request.getUri();
    }

    // TODO：没有适配文件类型，均以 text/html 返回
    public void sendStaticResource() throws IOException {
        File file = new File(WEB_ROOT, requestUri);

        if (file.exists()) {
            String okMessage = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n";
            out.write(okMessage.getBytes());

            byte[] bytes = new byte[1024];
            int len;
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            fis.close();
        } else {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            out.write(errorMessage.getBytes());
        }
    }
}

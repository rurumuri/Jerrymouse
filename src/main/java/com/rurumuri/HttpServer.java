package com.rurumuri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private ServerSocket ss;
    static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    private boolean shutdown = false;


    public HttpServer() {
        try {
            ss = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpServer(int port) {
        try {
            ss = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void await() {
        System.out.println("Server started on http://127.0.0.1:" + ss.getLocalPort());
        while (!shutdown) {
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                socket = ss.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                HttpRequest request = new HttpRequest(inputStream);
                request.parse();
                HttpResponse response = new HttpResponse(outputStream);
                response.setRequest(request);
                response.sendStaticResource();

                socket.close();
                if (request.getUri().equals(SHUTDOWN_COMMAND)) {
                    shutdown();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        shutdown = true;
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }
}

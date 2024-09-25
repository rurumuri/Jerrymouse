package com.rurumuri;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequest {

    private String httpMethod;
    private String uri;
    //    private BufferedReader reader;
    private InputStream input;


    public HttpRequest(InputStream inputStream) throws IOException {
//        reader = new BufferedReader(new InputStreamReader(inputStream));
        input = inputStream;
    }

    /*
     * 旧实现的问题：程序阻塞，持续等待输入；浏览器因得不到响应而持续等待（转圈）
     * 细说的话，具体是因为：
     * （1）浏览器发送请求后会等待响应，而后才会关闭 socket，其 InputStream 随之关闭；
     * （2）InputStream 未关闭，旧实现会持续读取导致阻塞，无法产生响应；浏览器得不到响应导致等待而不会关闭 socket，形成死锁。
     *
     * 值得研究的是 Connection: keep-alive 和 Connection: Close 的区别（不过不是导致这个问题的原因。需要验证。）
     * */
//    public void parse() throws IOException {
//        List<String> lines = reader.lines().toList();
//
//        for(String line : lines) {
//            System.out.println(line);
//        }
//
//        httpMethod = lines.get(0).split(" ")[0];
//        uri = lines.get(0).split(" ")[1];
//    }

    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        System.out.print(request.toString());
        uri = parseUri(request.toString());
    }

    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
}

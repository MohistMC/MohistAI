package com.mohistmc.ai.network;

import com.mohistmc.mjson.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class ApiController {

    public static void start(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHttpHandler());
        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
        System.out.println("HTTP Server started on port " + port);
    }

    static class MyHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String requestMethod = t.getRequestMethod();
            String requestPath = t.getRequestURI().getPath();

            if (requestMethod.equalsIgnoreCase("GET") && requestPath.equals("/")) {
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                String response = "Hello, Mohist AI!";
                os.write(response.getBytes());
                os.close();
            } else if (requestMethod.equalsIgnoreCase("POST") && requestPath.equals("/")) {
                t.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                StringBuilder requestBody = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        requestBody.append(line);
                    }
                }
                System.out.println(Json.read(requestBody.toString()));
                byte[] responseBytes = Json.read(requestBody.toString()).asBytes();
                System.out.println(Arrays.toString(responseBytes));
                t.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = t.getResponseBody();
                os.write(responseBytes);
                os.close();
            } else {
                t.sendResponseHeaders(404, 0);
                OutputStream os = t.getResponseBody();
                os.close();
            }
        }
    }
}

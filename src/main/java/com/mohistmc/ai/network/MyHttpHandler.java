package com.mohistmc.ai.network;

import com.mohistmc.ai.network.event.HttpPostEvent;
import com.mohistmc.mjson.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;

public class MyHttpHandler implements HttpHandler {
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
        } else if (requestMethod.equalsIgnoreCase("POST")) {
            t.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            RequestPath p = RequestPath.as(requestPath);
            if (p.isUnknown()) {
                close(t);
            } else {
                post(t, p);
            }
        } else {
            close(t);
        }
    }

    @SneakyThrows
    private void close(HttpExchange t) {
        t.sendResponseHeaders(404, 0);
        OutputStream os = t.getResponseBody();
        os.close();
    }

    @SneakyThrows
    private void post(HttpExchange t, RequestPath path) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        Json json = Json.read(requestBody.toString());
        HttpPostEvent event = new HttpPostEvent(this, json, path);
        ApiController.eventBus.onEvent(event);
        byte[] responseBytes = json.asBytes();
        t.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = t.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}

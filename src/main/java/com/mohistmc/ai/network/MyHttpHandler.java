package com.mohistmc.ai.network;

import com.mohistmc.ai.network.event.HttpGetEvent;
import com.mohistmc.ai.network.event.HttpPostEvent;
import com.mohistmc.mjson.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;

public class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        String requestMethod = t.getRequestMethod();
        String requestPath = t.getRequestURI().getPath();
        RequestPath p = RequestPath.as(requestPath);
        if (requestMethod.equalsIgnoreCase("GET")) {

            OutputStream os = t.getResponseBody();
            var json = Json.read("{\"Hello\": \"Mohist AI!\"}");
            HttpGetEvent event = new HttpGetEvent(this, json.toString().getBytes(StandardCharsets.UTF_8), p, requestPath);
            ApiController.eventBus.onEvent(event);
            if (event.getContenttype() == ContentType.JSON) {
                t.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                t.sendResponseHeaders(200, 0);
                os.write(event.getBytes());
            } else if (event.getContenttype() == ContentType.FILE) {
                Path filePath = event.getFile().toPath();
                t.getResponseHeaders().add("Content-Type", getContentType(filePath));
                t.getResponseHeaders().add("Content-Disposition", "attachment; filename=%s".formatted(event.getFile().getName()));
                t.sendResponseHeaders(200, Files.size(filePath));
                Files.copy(filePath, os);
            }
            os.close();
        } else if (requestMethod.equalsIgnoreCase("POST")) {
            t.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            if (p.isUnknown()) {
                close(t);
            } else {
                post(t, p);
            }
        } else {
            close(t);
        }
    }

    private String getContentType(Path path) {
        // 简单的内容类型判断，可根据扩展名返回相应的MIME类型
        String extension = path.toString().substring(path.toString().lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            default -> "application/octet-stream";
        };
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
        byte[] responseBytes = json.toString().getBytes(StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = t.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}

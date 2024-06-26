package com.mohistmc.ai.network;

import com.mohistmc.ai.sdk.BotType;
import com.mohistmc.mjson.Json;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpRequestUtils {

    private static final HttpClient client = HttpClient.newBuilder().build();

    public static CompletableFuture<String> post(BotType botType, String path, Map<String, String> body) {
        var json = Json.read(body);
        var request = HttpRequest.newBuilder().uri(URI.create(botType.getApi() + path))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).thenApply(HttpResponse::body).exceptionally(
                _ -> null
        );
    }
}

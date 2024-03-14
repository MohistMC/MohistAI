package com.mohistmc.ai.bots.gpt;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.mohistmc.ai.MohistConfig;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class OpenAI {

    public static Cache<Long, StringBuilder> CACHE;
    private static OpenAiService service;

    public static void init() {
        if (!MohistConfig.chatgpt) return;
        MohistAI.LOGGER.info("初始化ChatGPT...");
        service = new OpenAiService(MohistConfig.chatgpt_api_key, Duration.ZERO);
        CACHE = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .removalListener((RemovalListener<Long, StringBuilder>) notification -> {
                    if (notification.getKey() == null) {
                        return;
                    }
                    if (notification.getCause() == RemovalCause.EXPIRED) {
                        MohistAI.LOGGER.info("ChatGPT 已断开连接");
                    }
                }).build();
        MohistAI.LOGGER.info("初始化ChatGPT完毕...");

    }

    public static CompletableFuture<String> getResponse(StringBuilder cached, String message) {
        cached.append("\nHuman:").append(message).append("\nAI:");

        return CompletableFuture.supplyAsync(() -> {
            CompletionRequest request = CompletionRequest.builder()
                    .prompt(cached.toString())
                    .model("text-davinci-003")
                    .temperature(0.9)
                    .maxTokens(800)
                    .topP(1.0)
                    .frequencyPenalty(0.0)
                    .presencePenalty(0.6)
                    .stop(Arrays.asList("Human:", "AI:"))
                    .build();
            return service.createCompletion(request).getChoices().getFirst().getText();
        });
    }
}

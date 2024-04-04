package com.mohistmc.ai.bots.gpt;

public class OpenAI {

    /*
    public static Cache<Long, StringBuilder> CACHE;
    private static OpenAiService service;

    public static void init() {
        if (!MohistConfig.chatgpt) return;
        Log.info("初始化ChatGPT...");
        service = new OpenAiService(MohistConfig.chatgpt_api_key, Duration.ZERO);
        CACHE = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .removalListener((RemovalListener<Long, StringBuilder>) notification -> {
                    if (notification.getKey() == null) {
                        return;
                    }
                    if (notification.getCause() == RemovalCause.EXPIRED) {
                        Log.info("ChatGPT 已断开连接");
                    }
                }).build();
        Log.info("初始化ChatGPT完毕...");

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

     */
}

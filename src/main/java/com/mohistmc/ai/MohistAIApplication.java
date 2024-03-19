package com.mohistmc.ai;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.SolonMain;

@Slf4j
@Component
@SolonMain
public class MohistAIApplication {
    public static void main(String[] args) {
        Solon.start(MohistAIApplication.class, args, app -> {
            app.get("", ctx -> ctx.output("Hello Mohist AI!"));
        });
    }
}
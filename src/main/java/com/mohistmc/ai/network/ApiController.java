package com.mohistmc.ai.network;

import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.network.event.ListenRegister;
import com.mohistmc.ai.sdk.qq.QQPostListener;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;

public class ApiController {

    public static ListenRegister eventBus = ListenRegister.getInstance();

    public static void init() {
        ApiController api = new ApiController();
        api.start(MohistConfig.http_server_port.asInt());
        eventBus.registerListener(new QQPostListener());
    }

    @SneakyThrows
    public void start(int port) {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHttpHandler());
        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
        Log.info("HTTP Server started on port " + port);
    }
}

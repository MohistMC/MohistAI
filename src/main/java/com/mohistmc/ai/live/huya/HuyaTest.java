package com.mohistmc.ai.live.huya;

import com.mohistmc.ai.IOUtil;
import mjson.Json;

import java.io.IOException;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/7 3:30:59
 */
public class HuyaTest {

    public static void main(String[] args) throws IOException {
        String jsonText = IOUtil.readContent(IOUtil.getInputStream("https://www.huya.com/pinkfish")).split("TT_ROOM_DATA = ")[1].split("};")[0] + "}";
        Json json = Json.read(jsonText);
        System.out.println(jsonText);
        System.out.println("直播间: " + "https://www.huya.com/pinkfish");
        System.out.println("直播状态: " + (json.at("state").asString().equals("REPLAY") ? "重播中" : "直播中"));
        System.out.println("直播提醒: " + (json.at("isOn").asBoolean() ? "直播中" : "未开播"));
        System.out.println("标题: " + json.at("introduction").asString());
        new HuyaLive().run();
    }
}

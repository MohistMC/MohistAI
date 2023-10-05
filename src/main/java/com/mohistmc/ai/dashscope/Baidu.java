package com.mohistmc.ai.dashscope;

import com.mohistmc.ai.MohistConfig;
import java.io.IOException;
import mjson.Json;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/16 3:57:38
 */
public class Baidu {

    public static final String API_KEY = MohistConfig.baidu_apikey;
    public static final String SECRET_KEY = MohistConfig.baidu_secretkey;

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static String main(String args) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"role\":\"user\",\"content\":\"" + args + "\"}]}");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            Json json = Json.read(response.body().string());
            return json.asString("result");
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return Json.read(response.body().string()).asString("access_token");
    }
}

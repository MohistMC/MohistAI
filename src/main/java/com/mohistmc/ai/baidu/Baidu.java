package com.mohistmc.ai.baidu;

import com.mohistmc.ai.MohistConfig;
import java.io.IOException;
import json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/16 3:57:38
 */
public class Baidu {

    public String API_KEY;
    public String SECRET_KEY;

    public Baidu(String AK, String SK) {
        this.API_KEY = AK;
        this.SECRET_KEY = SK;
    }

    public String chat(BaiduSession session) {
        try {
           String token = getAccessToken();
            final String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + token;
            final JSONObject param = new JSONObject();
            param.put("messages", session.getHistory());
            param.put("system", "我是MohistMC的智能机器人，名叫小小墨。当前版本为QQ群专用，当然我也有实体版啦!");
            JSONObject result = sendRequest(url, param);
            MohistAI.LOGGER.info(result.toString());
            return result.get("result").toString();
        } catch (IOException e) {
            return null;
        }
    }

    public JSONObject sendRequest(String url, JSONObject param) {
        JSONObject result = new JSONObject();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity entity = new StringEntity(param.toString(), "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        try {
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            result.put("error", "连接错误！");
        }
        try {
            client.close();
        }
        catch (Exception e) {
            MohistAI.LOGGER.info(e.getMessage());
        }
        return result;
    }

    public String getAccessToken() throws IOException {
        final String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
        final JSONObject tokenResult = sendRequest(url, new JSONObject());
        return tokenResult.getString("access_token");
    }

    public static String aichat(BaiduSession session, String msg) {
        String resultMsg = "";
        session.addMessage("user", msg);
        try {
            Baidu ai = new Baidu(MohistConfig.baidu_apikey,MohistConfig.baidu_secretkey);
            final String res = ai.chat(session);
            session.addMessage("assistant", res);
            resultMsg += res;
        }
        catch (final Exception e) {
            e.printStackTrace();
            session.recall();
            return null;
        }
        return resultMsg;
    }
}

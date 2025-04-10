package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDeepseekLog;
import com.liwei.ruiyi.dao.impl.TDeepseekLogDaoImpl;
import com.liwei.ruiyi.model.SocketMessage;
import com.liwei.ruiyi.service.DeepseekService;
import com.liwei.ruiyi.socket.DeepseekSocket;
import com.liwei.ruiyi.utils.DateUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

@Repository("deepseekService")
public class DeepseekServiceImpl implements DeepseekService {

    private String DEEPSEEK_API_ENDPOINT = ReadProUtils.ReadProperties("deepseek.api.endpoint");
    private String DEEPSEEK_API_KEY = ReadProUtils.ReadProperties("deepseek.api.key");

    private static String sid = "";

    @Autowired
    TDeepseekLogDaoImpl deepseekLogDao;

    @Override
    public void sendMessage(String uid, String prompt) {
        this.sid = uid;

        TDeepseekLog log = new TDeepseekLog();
        log.setQuestion(prompt);
        log.setQt(DateUtils.getSystemTime());

        int insertId = deepseekLogDao.insert(log);

        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .build();


        // 使用JSONObject构建规范的JSON请求体
        JSONObject send = new JSONObject();
        send.put("model", "deepseek-chat");
        //deepseek-chat deepseek-coder
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        send.put("messages", new JSONObject[]{message});
        send.put("max_tokens", 4096);     // 最大回复长度
        send.put("temperature", 0.1);     // 降低随机性
        send.put("top_p", 0.1);           // 限制候选词范围
        send.put("frequency_penalty", 0); // 减少重复（0-1）
        send.put("presence_penalty", 0);  // 避免新话题（0-1）
        RequestBody body = RequestBody.create(
                send.toString(),
                MediaType.parse("application/json")
        );
        sendRightMessage("服务器收到向deepseek发送的消息，消息内容如下：\"" + prompt + "\"");
        sendRightMessage("等待deepseek回复消息，请稍后。");
        Request request = new Request.Builder()
                .url(DEEPSEEK_API_ENDPOINT)
                .addHeader("Authorization", "Bearer " + DEEPSEEK_API_KEY)
//                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            sendRightMessage(JSONObject.toJSONString(response));
            String answer = response.body().string();
            log.setAnswer(answer);
            log.setAt(DateUtils.getSystemTime());
            deepseekLogDao.update(log);
            JSONObject json = JSONObject.parseObject(answer);
            if (json.containsKey("choices")) {
                JSONObject messageJson = json.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
                String deepseek_answer = messageJson.getString("content");
                //分析返回的数据
//                analyzeResponse(deepseek_answer);
                sendLeftMessage(deepseek_answer);
            }


        } catch (SocketException e) {
            sendRightMessage("服务器收到向deepseek发送的消息，消息内容如下：\"" + prompt + "\"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRightMessage(String message) {
        if (StringUtils.isNotBlank(sid)) {
            SocketMessage socketMessage = new SocketMessage(SocketMessage.TYPE_MESSAGE, message, DateUtils.getSystemTime());
            DeepseekSocket.sendToAllClient(socketMessage, sid);
        }
    }

    private void sendLeftMessage(String message) {
        if (StringUtils.isNotBlank(sid)) {
            SocketMessage socketMessage = new SocketMessage(SocketMessage.TYPE_ANSWER, message, DateUtils.getSystemTime());
            DeepseekSocket.sendToAllClient(socketMessage, sid);
        }

    }

}

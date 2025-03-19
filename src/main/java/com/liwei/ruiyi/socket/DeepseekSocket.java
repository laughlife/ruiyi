package com.liwei.ruiyi.socket;

import com.alibaba.fastjson2.JSON;
import com.liwei.ruiyi.model.SocketMessage;
import com.liwei.ruiyi.service.DeepseekService;
import com.liwei.ruiyi.utils.DateUtils;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/deepseek/{sid}")
public class DeepseekSocket {

    private static DeepseekService deepseekService;

    @Qualifier("deepseekService")
    @Autowired
    public static void setDeepseekService(DeepseekService deepseekService) {
        DeepseekSocket.deepseekService = deepseekService;
    }

    private static final Map<String, Session> sessionMap = new HashMap();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        sessionMap.put(sid, session);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        SocketMessage messageJson = JSON.parseObject(message, SocketMessage.class);
        if(messageJson.getType().equals(SocketMessage.TYPE_PING)){
            SocketMessage receiveMessage = new SocketMessage(SocketMessage.TYPE_PONG, "", DateUtils.getSystemTime());
            sendToAllClient(receiveMessage,sid);
        }else{
            SocketMessage receiveMessage = new SocketMessage(SocketMessage.TYPE_MESSAGE, message, DateUtils.getSystemTime());
            sendToAllClient(receiveMessage,sid);
        }
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("连接断开:" + sid);
        sessionMap.remove(sid);
    }


    public static synchronized void sendToAllClient(SocketMessage sm, String clientId) {
        String message = JSON.toJSONString(sm);
        Session targetSession = sessionMap.get(clientId);
        if (targetSession == null || !targetSession.isOpen()) {
            sessionMap.remove(clientId);
            System.out.println("sessionID为：" + clientId + "的会话不存在.");
        } else {
            try {
                //服务器向客户端发送消息
                targetSession.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.err.println("向客户端发送消息时发生IO错误: " + e.getMessage());
                e.printStackTrace();
                // 从 sessionMap 中移除出现问题的会话
                sessionMap.remove(clientId);
            }catch (Exception e) {
                // 记录其他异常
                System.err.println("向客户端发送消息时发生错误: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}

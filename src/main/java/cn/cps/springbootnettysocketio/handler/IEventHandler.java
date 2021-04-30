package cn.cps.springbootnettysocketio.handler;

import cn.cps.springbootnettysocketio.conf.SocketioConfig;
import com.corundumstudio.socketio.SocketIOClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: Cai Peishen
 * @Date: 2021/4/28 17:40
 * @Description: 定义事件处理接口和默认实现
 * 连接的处理使用前端传入Authorization, 可以传入token, 做校验。
 */
public interface IEventHandler {
    void onConnect(SocketIOClient client);
    void onDisConnect(SocketIOClient client);
    
    
    default void connect(SocketIOClient client) {
//        if (!client.getNamespace().getName().equals("/chat")) {
//        client.disconnect();
//            return;
//        }
        String token = client.getHandshakeData().getSingleUrlParam("Authorization");
        if (token == null) {
            System.err.println("客户端" + client.getSessionId() + "建立websocket连接失败，Authorization不能为null");
            client.disconnect();
            return;
        }
        
        Map header = new HashMap<>();
        header.put("Authorization", token);
        
        String username = null;
//        try {
//            Map<String, Claim> claimMap = JWTUtils.verifyToken(token);
//            username = claimMap.get("username").asString();
//            if (username == null) {
//                throw new RuntimeException("websocket认证失败");
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            throw new RuntimeException("websocket认证失败", e);
//        } catch (ValidTokenException e) {
//            e.printStackTrace();
//            throw new RuntimeException("websocket认证失败", e);
//        }
        username = token;
        if (username != null) {
            System.out.println("客户端" + client.getSessionId() + "建立websocket连接成功");
            //将用户名和clientId对应 方便推送时候使用
            SocketioConfig.userClientIdMap.put(username, client.getSessionId());
        } else {
            System.err.println("客户端" + client.getSessionId() + "建立websocket连接失败");
            client.disconnect();
        }
        
    }
    
    default void disconnect(SocketIOClient client) {
        System.out.println("客户端" + client.getSessionId() + "断开websocket连接成功");
        //移除
        for (Map.Entry<String, UUID> entry : SocketioConfig.userClientIdMap.entrySet()) {
            if (Objects.equals(entry.getValue(), client.getSessionId())) {
                SocketioConfig.userClientIdMap.remove(entry.getKey());
            }
        }
    }
}

package cn.cps.springbootnettysocketio.handler.impl;

import cn.cps.springbootnettysocketio.handler.IEventHandler;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Cai Peishen
 * @Date: 2021/4/28 17:45
 * @Description:
 */
@Component(value= "testMessageEventHandler")
@ConditionalOnClass(SocketIOServer.class)
@Slf4j
public class TestMessageEventHandler implements IEventHandler {
    private final SocketIOServer socketIOServer;
    private static int testPushCount = 0;
    
    private String namespace = "/test";
    
    public TestMessageEventHandler(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }
    
    @Override
    @OnConnect
    public void onConnect(SocketIOClient client) {
        connect(client);
    }
    
    
    @Override
    @OnDisconnect
    public void onDisConnect(SocketIOClient client) {
        disconnect(client);
    }
    
    @OnEvent(value = "aaaa")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        log.debug("发来消息：" + data);
        UUID sessionId = client.getSessionId();
        socketIOServer.getNamespace(namespace).getClient(sessionId).sendEvent("bbbb", "点对点消息的返回" + Math.random());
    }
    
    /**
     * 测试无限推送
     * */
    @OnEvent(value = "testPush")
    public void onTestPushEvent(SocketIOClient client, AckRequest request, String data) {
        UUID sessionId = client.getSessionId();
        Runnable runnable = () -> {
            testPushCount++;
            int thisTestPushCount = testPushCount;
            for (; ; ) {
                if (thisTestPushCount < testPushCount) {
                    break;
                }
                socketIOServer.getNamespace(namespace).getClient(sessionId).sendEvent("testPush", new Random().nextInt(5));
                try {
                    TimeUnit.MILLISECONDS.sleep(900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
    
    /**
     * 测试加入房间
     * */
    @OnEvent(value = "joinRoom")
    public void onTestJoinRoomEvent(SocketIOClient client, AckRequest request, String data) {
        client.leaveRoom(data);
        client.joinRoom(data);
    }
    
    /**
     * 测试房间发送信息(类似于订阅式广播消息)
     * */
    @OnEvent(value = "testRoom")
    public void onTestRoomEvent(SocketIOClient client, AckRequest request, String data) {
        socketIOServer.getNamespace(namespace).getRoomOperations("room1").sendEvent("testRoom", "房间里的消息" + Math.random());
    }
    
    /**
     * 测试发送广播消息
     * */
    @OnEvent(value = "testBroadcast")
    public void onTestBroadcastEvent(SocketIOClient client, AckRequest request, String data) {
        socketIOServer.getNamespace(namespace).getBroadcastOperations().sendEvent("testBroadcast", "广播的消息" + Math.random());
    }
    
}

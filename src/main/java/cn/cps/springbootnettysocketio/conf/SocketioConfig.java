package cn.cps.springbootnettysocketio.conf;


import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Cai Peishen
 * @Date: 2021/4/28 17:39
 * @Description:
 */
// 使用注解方式决定是否要开启
//@Configuration
public class SocketioConfig {
    @Value("${socket.io.port:8974}")
    private int socketIoPort;
    @Value("${socket.io.namespaces}")
    private String[] namespaces;
    
    /**用户名和websocket clientId 对应关系*/
    public static Map<String, UUID> userClientIdMap = new ConcurrentHashMap<>();
    
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config =
                new com.corundumstudio.socketio.Configuration();
        config.setOrigin(null);   // 注意如果开放跨域设置，需要设置为null而不是"*"
        config.setPort(socketIoPort);
        config.setSocketConfig(new SocketConfig());
        config.setWorkerThreads(100);
        config.setAuthorizationListener(handshakeData -> true);
        //允许最大帧长度
        config.setMaxFramePayloadLength(1024 * 1024);
        //允许下最大内容
        config.setMaxHttpContentLength(1024 * 1024);
        final SocketIOServer server = new SocketIOServer(config);
        Optional.ofNullable(namespaces).ifPresent(nss ->
                Arrays.stream(nss).forEach(server::addNamespace));
//        server.start();
        return server;
        
    }
    
    /**
     * 注入OnConnect，OnDisconnect，OnEvent注解。 不写的话Spring无法扫描OnConnect，OnDisconnect等注解
     * */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer){
        return new SpringAnnotationScanner(socketIOServer);
    }
}

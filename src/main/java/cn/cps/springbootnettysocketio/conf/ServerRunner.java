package cn.cps.springbootnettysocketio.conf;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: Cai Peishen
 * @Date: 2021/4/28 17:42
 * @Description: 定义启动类
 * 这里为每个命名空间注册一个事件处理Handler, 规则为bean的name为命名空间名称+MessageHandler。
 */
@Component
public class ServerRunner implements CommandLineRunner {
    @Autowired(required = false)
    private SocketIOServer socketIOServer;
    
    @Value("${socket.io.namespaces}")
    private String[] namespaces;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    public void run(String... args) throws Exception {
        if (socketIOServer != null) {
            /*Optional.ofNullable(SpringService.getBean("messageEventHandler"))
                    .ifPresent(handler -> socketIOServer.getNamespace("/").addListeners(handler));*/
            
            
            Optional.ofNullable(namespaces).ifPresent(nss ->
                    Arrays.stream(nss).forEach(ns -> {
                        //获取命名空间
                        SocketIONamespace socketIONamespace = socketIOServer.getNamespace(ns);
                        //获取期待的类名
                        String className = ns.substring(1) + "MessageEventHandler";
                        try {
                            Object bean = applicationContext.getBean(className);
                            Optional.ofNullable(bean).ifPresent(socketIONamespace::addListeners);
                        } catch (Exception e) {
                        
                        }
                        
                    }));
//            socketIOServer.getNamespace("/chat").addListeners(messageEventHandler);
            socketIOServer.start();
        }
    }
}

package cn.cps.springbootnettysocketio.annotaion;

import cn.cps.springbootnettysocketio.conf.SocketioConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: Cai Peishen
 * @Date: 2021/4/28 17:49
 * @Description: socketIo服务器开关
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SocketioConfig.class)
public @interface EnableSocketio {
}

package cn.cps.springbootnettysocketio.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Cai Peishen
 * @Date: 2021/4/28 17:53
 * @Description:
 */
@Controller
public class SocketioController {
    
    @RequestMapping("/chat")
    public String chat() {
        return "chat.html";
    }
    
    @RequestMapping("/test")
    public String test() {
        return "test.html";
    }
    
}

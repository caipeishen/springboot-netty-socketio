package cn.cps.springbootnettysocketio;

import cn.cps.springbootnettysocketio.annotaion.EnableSocketio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSocketio
@SpringBootApplication
public class SpringbootNettySocketioApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringbootNettySocketioApplication.class, args);
    }
    
}

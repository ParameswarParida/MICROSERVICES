package com.example.greet_api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.greet_api.contoller.GreetApiContoller;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GreetApiApplicationTests {

    @Autowired
    private GreetApiContoller greetApiContoller;

    @Test
    void contextLoads() {
        assertTrue(AopUtils.isAopProxy(greetApiContoller),
                "Resilience4j retry aspect should proxy the greet controller");
    }
}

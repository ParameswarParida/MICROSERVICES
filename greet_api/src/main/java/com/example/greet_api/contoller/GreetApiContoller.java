package com.example.greet_api.contoller;

import com.example.greet_api.feignClient.WelcomeFeignClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/greet")
public class GreetApiContoller {

    @Autowired
    private WelcomeFeignClient welcomeFeignClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(
            name = "welcomeService",
            fallbackMethod = "welcomeFallback"
    )
    @GetMapping
    public String greet() {
        System.out.println("Greet API called");

        String output = welcomeFeignClient.getWelcomeMsg();
        return "Good morning " + output;
    }

    public String welcomeFallback(Throwable exception) {
        System.out.println("Welcome Service fallback called");
        System.out.println(exception.getMessage());

        return "Welcome Service is currently unavailable";
    }

    @GetMapping("/circuit-status")
    public Map<String, Object> getCircuitStatus() {

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker("welcomeService");

        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("service", "welcomeService");
        response.put("state", circuitBreaker.getState().name());
        response.put("failureRate", metrics.getFailureRate());
        response.put("bufferedCalls", metrics.getNumberOfBufferedCalls());
        response.put("failedCalls", metrics.getNumberOfFailedCalls());
        response.put("successfulCalls", metrics.getNumberOfSuccessfulCalls());
        response.put("notPermittedCalls", metrics.getNumberOfNotPermittedCalls());

        return response;
    }
}
package com.example.welcome_service.controller;

import com.example.welcome_service.service.WelcomeEventProducer;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    private final Environment environment;
    private final WelcomeEventProducer eventProducer;

    public WelcomeController(Environment environment, WelcomeEventProducer eventProducer) {
        this.environment = environment;
        this.eventProducer = eventProducer;
    }

    @GetMapping
    public String welcome() {
        System.out.println("Welcome service is running");
        String port = environment.getProperty("server.port");
        return "welcome Param from " + port;
    }

    @PostMapping("/events")
    public CompletableFuture<ResponseEntity<String>> publishWelcomeEvent(
            @RequestBody WelcomeEventRequest request) {
        if (request == null || request.message() == null || request.message().isBlank()) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body("message must not be blank"));
        }

        return eventProducer.publish(request.message())
                .thenApply(result -> ResponseEntity.accepted().body(
                        "Published to " + result.getRecordMetadata().topic()
                                + " at offset " + result.getRecordMetadata().offset()));
    }

    public record WelcomeEventRequest(String message) {
    }
}

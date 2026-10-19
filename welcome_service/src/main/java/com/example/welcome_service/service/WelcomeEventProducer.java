package com.example.welcome_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.CompletableFuture;

@Service
public class WelcomeEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public WelcomeEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public CompletableFuture<SendResult<String, String>> publish(String message) {
        return kafkaTemplate.send(topic, message);
    }
}

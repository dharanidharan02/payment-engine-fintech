package com.fintech.payment_engine.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "payment-events", groupId = "payment-engine-group")
    public void consume(String message) {
        System.out.println("Kafka consumer received event: " + message);
    }
}
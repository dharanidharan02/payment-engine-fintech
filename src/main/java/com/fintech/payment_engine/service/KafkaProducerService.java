package com.fintech.payment_engine.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "payment-events";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String message) {
        kafkaTemplate.send(TOPIC, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.out.println("Failed to send Kafka event: " + ex.getMessage());
                    } else {
                        System.out.println("Kafka event sent successfully: " + message);
                    }
                });
    }
}
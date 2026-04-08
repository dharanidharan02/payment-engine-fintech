package com.fintech.payment_engine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "payment-events", groupId = "payment-engine-group")
    public void consume(String message) {
        try {
            log.info("Kafka consumer received event: {}", message);

        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
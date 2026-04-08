package com.fintech.payment_engine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "payment-events";
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String message) {
        kafkaTemplate.send(TOPIC, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                      log.error("Failed to send Kafka event: " + ex.getMessage());
                    } else {
                       log.info("Kafka event sent successfully: " + message);
                    }
                });
    }
}
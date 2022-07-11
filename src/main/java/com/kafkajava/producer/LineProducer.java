package com.kafkajava.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class LineProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(LineProducer.class);


    public LineProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String rawData, Properties properties) {
        try {
            kafkaTemplate.send(properties.getProperty("spring.boot.kafka.input.topic.name"), objectMapper.writeValueAsString(rawData));
        } catch (JsonProcessingException e) {
            logger.error("Error while serializing " + rawData, e);
        }
    }
}

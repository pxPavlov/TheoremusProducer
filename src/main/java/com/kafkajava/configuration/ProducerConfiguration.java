package com.kafkajava.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ProducerConfiguration {
    private static final String KAFKA_BROKER = "localhost:9092";

    @Bean
    public ProducerFactory<String, String> producerFactory() throws IOException {
        return new DefaultKafkaProducerFactory<>(getProducerConfigurations());
    }

    @Bean
    public Map<String, Object> getProducerConfigurations() throws IOException {
        Map<String, Object> configurations = new HashMap<>();
        Properties properties = ProducerConfiguration.getAppProperties();

        configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty("spring.boot.kafka.bootstrap.servers"));
        configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configurations.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        return configurations;
    }

    @Bean
    public static Properties getAppProperties() throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream("configuration/application.properties");
        prop.load(input);

        return prop;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() throws IOException {
        return new KafkaTemplate<>(producerFactory());
    }
}

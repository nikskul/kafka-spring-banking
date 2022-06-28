package com.nikskul.kafkaspringbanking.config;

import com.nikskul.kafkaspringbanking.request.DepositRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public ProducerFactory<String, DepositRequest> depositProducerFactory() {
        return properties();
    }

    @Bean
    public <V> ProducerFactory<String, V> generalizedProducerFactory() {
        return properties();
    }

    private <V> ProducerFactory<String, V> properties() {
        HashMap<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public <V> KafkaTemplate<String, V> generalizedKafkaTemplate(
            ProducerFactory<String, V> generalizedProducerFactory
    ) {
        return new KafkaTemplate<>(generalizedProducerFactory);
    }

    @Bean
    public KafkaTemplate<String, DepositRequest> kafkaDepositTemplate(
            ProducerFactory<String, DepositRequest> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}

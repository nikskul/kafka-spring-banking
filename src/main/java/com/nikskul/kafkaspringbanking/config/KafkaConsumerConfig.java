package com.nikskul.kafkaspringbanking.config;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> defaultContainerFactory(
            ConsumerFactory<String, String> depositConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(depositConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> defaultConsumerFactory() {
        Map<String, Object> properties = properties();

        return new DefaultKafkaConsumerFactory<>(
                properties,
                new StringDeserializer(),
                new StringDeserializer()
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OperationRequest> clientRequestContainerFactory(
            ConsumerFactory<String, OperationRequest> operationConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OperationRequest> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(operationConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OperationRequest> operationConsumerFactory() {
        Map<String, Object> properties = properties();

        return new DefaultKafkaConsumerFactory<>(
                properties,
                new StringDeserializer(),
                new JsonDeserializer<>(OperationRequest.class)
        );
    }

    private Map<String, Object> properties() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return props;
    }
}

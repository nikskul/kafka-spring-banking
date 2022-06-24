package com.nikskul.kafkaspringbanking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic balanceChangeEventTopic(
            @Value("${topics.balance}") String balanceEventTopic
    ) {
        return TopicBuilder.name(balanceEventTopic)
                .build();
    }

    @Bean
    public NewTopic compactClientTopic(
            @Value("${topics.client}") String clientTopic
    ) {
        return TopicBuilder.name(clientTopic)
                .compact()
                .build();
    }
}

package com.nikskul.kafkaspringbanking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic depositsTopic(
            @Value("${topics.deposit}") String depositsTopic
    ) {
        return TopicBuilder.name(depositsTopic)
                .build();
    }

    @Bean
    public NewTopic withdrawalsTopic(
            @Value("${topics.withdrawal}") String withdrawalsTopic
    ) {
        return TopicBuilder.name(withdrawalsTopic)
                .build();
    }

    @Bean
    public NewTopic bankClientsTopic(
            @Value("${topics.client}") String bankClientsTopic
    ) {
        return TopicBuilder.name(bankClientsTopic)
                .compact()
                .build();
    }
}

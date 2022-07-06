package com.nikskul.kafkaspringbanking.produser;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9091",
                "port=9091",
                "auto.create.topics.enable=false",
                "delete.topic.enable=true"
        }
)
class KafkaProducerTest {

    private final String topic = "operation-producer-test-topic";

    @Autowired
    OperationRequestProducer producer;

    final BlockingQueue<ConsumerRecord<String, OperationRequest>> records = new LinkedBlockingQueue<>();

    @Autowired
    ConsumerFactory<String, OperationRequest> consumerFactory;

    MessageListenerContainer getRequestContainer() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testing-operation-producer");

        consumerFactory.updateConfigs(props);

        var container = new ConcurrentMessageListenerContainer<>(consumerFactory, new ContainerProperties(topic));

        container.setupMessageListener(
                (MessageListener<String, OperationRequest>) records::add
        );

        return container;

    }

    @Test
    void shouldSendToKafkaSuccess() throws InterruptedException {

        var container = getRequestContainer();

        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        var request = new OperationRequest(
                "test",
                BigDecimal.TEN
        );

        producer.sendToKafka(topic, request);

        var received = records.poll(10, TimeUnit.SECONDS);

        assertThat(received, hasKey(request.getClientName()));
        assertThat(received, hasValue(request));

        container.stop();
    }

    @Test
    void shouldThrowExceptionWhenSendNegativeValue() throws InterruptedException {

        var container = getRequestContainer();

        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        var request = new OperationRequest(
                "test",
                BigDecimal.TEN.negate()
        );

        assertThrows(RuntimeException.class, () -> producer.sendToKafka(topic, request));

        var received = records.poll(10, TimeUnit.SECONDS);

        assertNull(received);

        container.stop();

    }
}
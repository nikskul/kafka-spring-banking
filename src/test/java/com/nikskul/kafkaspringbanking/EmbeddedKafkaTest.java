package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.model.BankClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9091",
                "port=9091",
                "auto.create.topics.enable=false",
                "delete.topic.enable=true"
        }
)
public class EmbeddedKafkaTest {

    @Value("testing-kafka")
    String clientTopic;

    @Autowired
    KafkaTemplate<String, BankClient> clientKafkaTemplate;

    final BlockingQueue<ConsumerRecord<String, BankClient>> records = new LinkedBlockingQueue<>();

    KafkaMessageListenerContainer<String, BankClient> getBankClientListenerContainer(String topic) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testing-client-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        var factory = new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(BankClient.class)
        );

        var listener = new KafkaMessageListenerContainer<>(factory, new ContainerProperties(topic));

        listener.setupMessageListener(
                (MessageListener<String, BankClient>) records::add
        );

        return listener;
    }

    @Test
    void testKafkaClientSendAndReceive() throws InterruptedException {

        var container = getBankClientListenerContainer(clientTopic);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        BankClient client = new BankClient(
                "test1",
                "test",
                "Hello",
                "Kafka",
                "Test"
        );

        clientKafkaTemplate.send(clientTopic, client.getUsername(), client);

        var receive = records.poll(10, TimeUnit.SECONDS);

        assertThat(receive, hasKey(client.getUsername()));

        container.stop();
    }

}

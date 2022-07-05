package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.model.BankClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

@SpringBootTest
@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9091",
                "port=9091",
                "auto.create.topics.enable=false",
                "delete.topic.enable=true"
        }
)
class BankClientServiceTest {

    String clientTopic;

    private final BankClientService service;

    final BlockingQueue<ConsumerRecord<String, BankClient>> records = new LinkedBlockingQueue<>();

    @Autowired
    public BankClientServiceTest(
            @Value("testing-client-service") String clientTopic,
            KafkaTemplate<String, BankClient> template
    ) {
        this.clientTopic = clientTopic;
        this.service = new BankClientService(clientTopic, template);
    }

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
    @DirtiesContext
    void testRegister() throws InterruptedException {

        var container = getBankClientListenerContainer(clientTopic);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        BankClient client = new BankClient(
                "register",
                "test",
                "test",
                "test",
                "test"
        );

        service.register(client);

        var receive = records.poll(10, TimeUnit.SECONDS);

        assertThat(receive, hasKey(client.getUsername()));
        assertThat(receive, hasValue(client));

        container.stop();
    }
}
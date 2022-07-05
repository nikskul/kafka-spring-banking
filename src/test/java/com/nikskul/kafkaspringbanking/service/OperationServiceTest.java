package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import kafka.admin.AdminUtils;
import kafka.admin.AdminUtils$;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientTestUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

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
class OperationServiceTest {

    String depositTopic;
    String withdrawalTopic;

    @MockBean
    BankClientService clientService;

    private final OperationService service;

    final BlockingQueue<ConsumerRecord<String, OperationRequest>> records = new LinkedBlockingQueue<>();

    @Autowired
    public OperationServiceTest(
            BankClientService clientService,
            @Value("testing-deposit-service") String depositTopic,
            @Value("testing-withdrawal-service") String withdrawalTopic,
            KafkaTemplate<String, OperationRequest> template
    ) {
        this.depositTopic = depositTopic;
        this.withdrawalTopic = withdrawalTopic;
        this.service = new OperationService(
                clientService,
                depositTopic,
                withdrawalTopic,
                template
        );
    }

    KafkaMessageListenerContainer<String, OperationRequest> getOperationRequestListenerContainer(String topic) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testing-operation-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        var factory = new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(OperationRequest.class)
        );

        var listener = new KafkaMessageListenerContainer<>(factory, new ContainerProperties(topic));

        listener.setupMessageListener(
                (MessageListener<String, OperationRequest>) records::add
        );

        return listener;
    }

    @Test
    void makeDeposit() throws InterruptedException {
        var container = getOperationRequestListenerContainer(depositTopic);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        OperationRequest request = new OperationRequest(
                "register",
                "test",
                BigDecimal.TEN
        );


        Mockito.when(clientService.findByUsername(request.getUsername()))
                .thenReturn(
                        Optional.of(
                                new BankClient(
                                        request.getUsername(),
                                        request.getPassword(),
                                        "test",
                                        "test",
                                        "test"
                                )
                        )
                );

        service.makeDeposit(request);

        var receive = records.poll(10, TimeUnit.SECONDS);

        assertThat(receive, hasKey(request.getUsername()));
        assertThat(receive, hasValue(request));

        container.stop();
    }

    @Test
    void makeWithdrawal() throws InterruptedException {
        var container = getOperationRequestListenerContainer(withdrawalTopic);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);

        OperationRequest request = new OperationRequest(
                "register",
                "test",
                BigDecimal.TEN
        );


        Mockito.when(clientService.findByUsername(request.getUsername()))
                .thenReturn(
                        Optional.of(
                                new BankClient(
                                        request.getUsername(),
                                        request.getPassword(),
                                        "test",
                                        "test",
                                        "test"
                                )
                        )
                );

        service.makeWithdrawal(request);

        var receive = records.poll(10, TimeUnit.SECONDS);

        assertThat(receive, hasKey(request.getUsername()));
        assertThat(receive, hasValue(request));

        container.stop();
    }
}
package com.enisspahi.example;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;

import static com.enisspahi.example.GreetingConsumerContractTests.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;

@PactConsumerTest
@PactTestFor(providerName = PROVIDER, providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V3)
@SpringBootTest
public class GreetingConsumerContractTests {

    static final String CONSUMER = "greetingConsumer";
    static final String PROVIDER = "greetingProducer";

    @Autowired
    private MyKafkaListener myKafkaListener;

    @MockBean
    private MyRepository myRepository;

    @Pact(consumer = CONSUMER)
    MessagePact greetingPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringMatcher("message", "Hello.*");
        return builder
                .expectsToReceive("A greeting message")
                .withContent(body)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "greetingPact")
    void greetingTest(List<Message> messages) {
        messages.forEach(message -> {
            var greeting = parse(message);
            myKafkaListener.listen(greeting);
            verify(myRepository).save(greeting);
        });
    }

    private static Greeting parse(Message message) {
        try {
            return new ObjectMapper().readValue(message.contentsAsBytes(), Greeting.class);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected");
        }
    }

}

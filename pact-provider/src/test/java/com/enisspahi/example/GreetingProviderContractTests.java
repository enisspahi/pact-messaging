package com.enisspahi.example;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Provider("greetingProducer")
@PactBroker(url = "http://localhost:9292/")
public class GreetingProviderContractTests {

    @Autowired
    GreetingProducer greetingProducer;

    @MockBean
    KafkaTemplate<String, Greeting> kafkaTemplate;

    @BeforeEach
    void setTargetToMessage(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @PactVerifyProvider("A greeting message")
    String verifyGreetingMessage() {
        greetingProducer.sendGreeting();
        ArgumentCaptor<Greeting> captor = ArgumentCaptor.forClass(Greeting.class);
        Mockito.verify(kafkaTemplate).send(anyString(), captor.capture());
        var greeting = captor.getValue();
        return toJsonString(greeting);
    }

    private static String toJsonString(Greeting greeting) {
        try {
            return new ObjectMapper().writeValueAsString(greeting);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unexpected");
        }
    }

}

package com.enisspahi.example;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class GreetingProducer {

    private final KafkaTemplate<String, Greeting> kafkaTemplate;

    public GreetingProducer(KafkaTemplate<String, Greeting> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendGreeting() {
        kafkaTemplate.send( "greetings-topic", new Greeting("Hello"));
    }

}

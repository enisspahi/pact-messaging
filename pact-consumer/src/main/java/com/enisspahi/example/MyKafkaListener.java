package com.enisspahi.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaListener {

    private final Logger logger = LoggerFactory.getLogger(MyKafkaListener.class);

    private final MyRepository myRepository;

    public MyKafkaListener(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    @KafkaListener(topics = "greetings-topic", groupId = "my-kafka-listener")
    public void listen(Greeting greeting) {
        myRepository.save(greeting);
        logger.info("Greeting message '{}' stored", greeting.message());
    }

}

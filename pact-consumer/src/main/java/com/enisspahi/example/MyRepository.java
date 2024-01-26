package com.enisspahi.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyRepository {

    private final Logger logger = LoggerFactory.getLogger(MyRepository.class);

    public Greeting save(Greeting greeting) {
        logger.info("I just stored '{}' message", greeting);
        return greeting;
    }
}

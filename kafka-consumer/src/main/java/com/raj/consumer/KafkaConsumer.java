package com.raj.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {


    @KafkaListener(topics = "sports-news", groupId = "sp-group-1")
    public void consume(String message) {

       log.info("Message consume : {}",message);
    }

}

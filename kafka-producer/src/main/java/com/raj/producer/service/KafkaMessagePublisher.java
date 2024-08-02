package com.raj.producer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher {


    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${myapp.topic}")
    private String topicName;

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> status = kafkaTemplate.send(topicName, message);
        status.whenComplete((result, throwable) -> {
            if (throwable == null) {
                System.out.println("Sent message ["+message+"] " +
                        "with offset ["+result.getRecordMetadata().offset()+"]");
            } else {
                System.out.println("Unable to send message to kafka topic ["+topicName+"] " +
                        "due to "+throwable.getMessage());
            }
        });
    }
}

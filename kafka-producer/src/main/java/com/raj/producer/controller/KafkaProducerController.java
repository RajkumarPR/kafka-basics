package com.raj.producer.controller;


import com.raj.producer.service.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/producer")
public class KafkaProducerController {


    private final KafkaMessagePublisher kafkaMessagePublisher;

    @PostMapping
    public ResponseEntity<String> producerMsg(@RequestParam("message") String message) {
        kafkaMessagePublisher.sendMessage(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

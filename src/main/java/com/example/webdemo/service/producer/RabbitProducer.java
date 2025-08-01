package com.example.webdemo.service.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@RestController
public class RabbitProducer {

    private RabbitTemplate rabbitTemplate;

    private RabbitAdmin rabbitAdmin;

    @GetMapping(value = "/send")
    public void sendMsg() {
        rabbitTemplate.convertAndSend(
                // 原有topic交换机，相同routingKey实现多队列同时消费功能
                "topicExA",
                // 通过新的fanout交换机，实现多队列同时消费功能
                //"fanExAb",
                "rkA",
                "test_repeatedly_consume_" + Instant.now().toEpochMilli()
        );
    }

    @GetMapping(value = "/send-receive")
    public void sendAndReceive() {
        rabbitTemplate.convertAndSend(
                // 原有topic交换机，相同routingKey实现多队列同时消费功能
                "topicExA",
                "rkA",
                "test_send_and_receive_" + Instant.now().toEpochMilli()
        );

        Message receive = rabbitTemplate.receive("queueA", 10_000);
        if (receive != null && receive.getBody() != null) {
            String msg = new String(receive.getBody(), StandardCharsets.UTF_8);
            log.info(msg);
        }
    }

    @GetMapping(value = "/send-consume")
    public void sendAndConsume() {
        rabbitTemplate.convertAndSend(
                // 原有topic交换机，相同routingKey实现多队列同时消费功能
                "topicExA",
                "rkB",
                "test_send_and_consume_" + Instant.now().toEpochMilli()
        );
    }

    @GetMapping(value = "/declare-queue")
    public void declareQueue() {
        rabbitAdmin.declareQueue();
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }
}

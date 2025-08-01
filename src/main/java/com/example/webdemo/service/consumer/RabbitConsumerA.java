package com.example.webdemo.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
public class RabbitConsumerA {

    @RabbitHandler
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "queueA", durable = "true"),
                    exchange = @Exchange(value = "topicExA", type = ExchangeTypes.TOPIC),
                    key = "rkA"
            )
    })
    public void process(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("{} 收到消息消息：{}", this.getClass().getName(), msg);
    }
}

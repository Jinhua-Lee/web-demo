package com.example.webdemo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jinhua-Lee
 */
@Configuration
public class RepeatedlyConsumeWithRoutingKeyConfig {

    @Bean
    public TopicExchange exA(@Value(value = "topicExA") String exchangeNameA) {
        return new TopicExchange(exchangeNameA, true, false);
    }

    @Bean
    public Queue queueA(@Value(value = "queueA") String queueNameA) {
        return new Queue(queueNameA, true, false, false);
    }

    @Bean
    public Binding oriBinding(Queue queueA, TopicExchange exA, @Value(value = "rkA") String oriRoutingKey) {
        return BindingBuilder.bind(queueA).to(exA).with(oriRoutingKey);
    }

    @Bean
    public Queue queueB(@Value(value = "queueB") String queueNameB) {
        return new Queue(queueNameB, true, false, false);
    }

    @Bean
    public Binding newBinding(Queue queueB, TopicExchange exA, @Value(value = "rkA") String oriRoutingKey) {
        return BindingBuilder.bind(queueB).to(exA).with(oriRoutingKey);
    }
}

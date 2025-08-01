package com.example.webdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author Jinhua-Lee
 */
@Configuration
@DependsOn(value = "repeatedlyConsumeWithRoutingKeyConfig")
@ConditionalOnMissingBean(value = RepeatedlyConsumeWithRoutingKeyConfig.class)
public class RepeatedlyConsumeWithFanoutExchangeConfig {
    @Bean
    public TopicExchange exA(@Value(value = "topicExA") String exchangeNameA) {
        return new TopicExchange(exchangeNameA, true, false);
    }

    @Bean
    public Queue queueA(@Value(value = "queueA") String queueNameA) {
        return new Queue(queueNameA, true, false, false);
    }

    @Bean
    public Binding oriBinding(Queue queueA, TopicExchange exA,
                              @Value(value = "rkA") String oriRoutingKey) {
        return BindingBuilder.bind(queueA).to(exA).with(oriRoutingKey);
    }

    @Bean
    public Queue queueB(@Value(value = "queueB")String queueNameB) {
        return new Queue(queueNameB, true, false, false);
    }

    @Bean
    public FanoutExchange exAb(@Value(value = "fanExAb") String fanoutExchangeName) {
        return new FanoutExchange(fanoutExchangeName, true, false);
    }

    @Bean
    public Binding transFromBinding(Queue queueA, FanoutExchange exAb) {
        return BindingBuilder.bind(queueA).to(exAb);
    }

    @Bean
    public Binding transToBinding(Queue queueB, FanoutExchange exAb) {
        return BindingBuilder.bind(queueB).to(exAb);
    }
}

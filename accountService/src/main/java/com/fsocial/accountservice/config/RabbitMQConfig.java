package com.fsocial.accountservice.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.profile.create}")
    String createProfileQueue;

    @Value("${rabbitmq.exchange.profile.create}")
    String profileExchange;

    @Value("${rabbitmq.routing.profile.create}")
    String createProfileRoutingKey;

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue profileCreateQueue() {
        return new Queue(createProfileQueue, true);
    }

    @Bean
    public DirectExchange profileExchange() {
        return new DirectExchange(profileExchange);
    }

    // Binding profile create
    @Bean
    public Binding profileCreateBinding(Queue profileCreateQueue, DirectExchange profileExchange) {
        return BindingBuilder.bind(profileCreateQueue).to(profileExchange).with(createProfileRoutingKey);
    }

}

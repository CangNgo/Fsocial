package com.fsocial.accountservice.config;

import com.fsocial.accountservice.enums.exchange.Exchange;
import com.fsocial.accountservice.enums.queue.ProfileQueue;
import com.fsocial.accountservice.enums.routingkey.ProfileRoutingKey;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue profileCreateQueue() {
        return new Queue(ProfileQueue.PROFILE_CREATE.getQueue(), true);
    }

    @Bean
    public DirectExchange profileExchange() {
        return new DirectExchange(Exchange.PROFILE.getExchange());
    }

    // Binding profile create
    @Bean
    public Binding profileCreateBinding(Queue profileCreateQueue, DirectExchange profileExchange) {
        return BindingBuilder.bind(profileCreateQueue).to(profileExchange).with(ProfileRoutingKey.CREATE.getRoutingKey());
    }

}

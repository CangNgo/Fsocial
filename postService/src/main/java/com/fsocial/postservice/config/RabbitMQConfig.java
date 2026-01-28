package com.fsocial.postservice.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.post.comment.delete}")
    String queueCommentDelete;

    @Value("${rabbitmq.queue.post.attachments.delete}")
    String queueCommentAttachments;

    @Value("${rabbitmq.exchange.post.delete}")
    String exchangePostDelete;

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue postCommentDeleteQueue() {
        return new Queue(queueCommentDelete, true);
    }

    @Bean
    public Queue postCommentAttachmentsQueue() {
        return new Queue(queueCommentAttachments, true);
    }

    @Bean
    public FanoutExchange postDeleteExchange(){
        return new FanoutExchange(exchangePostDelete);
    }

    //Binding delete comment
    @Bean
    public Binding postCommentDeleteBinding(){
        return BindingBuilder.bind(postCommentDeleteQueue()).to(postDeleteExchange());
    }

    //Binding delete attachments
    @Bean
    public Binding postCommentAttachmentsBinding(){
        return BindingBuilder.bind(postCommentAttachmentsQueue()).to(postDeleteExchange());
    }
}

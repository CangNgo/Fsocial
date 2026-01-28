package com.fsocial.postservice.publisher;

import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.dto.post.PostDTORequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostEventPublisher {

    String queueCommentAttachments;

    @Value("${rabbitmq.exchange.post.delete}")
    String exchangePostDelete;

    final RabbitTemplate rabbitTemplate;

    //Delete post
    public void eventDeletePost(String postId){
        rabbitTemplate.convertAndSend(exchangePostDelete,"",postId);
    }

}

package com.fsocial.postservice.comsumer;

import com.fsocial.postservice.dto.post.PostDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Component
@RequiredArgsConstructor
@Slf4j
public class AttachmentsConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.post.attachments.delete}")
    public void receiveAttachments(String postId){

        log.info("Received attachment for post with id: " + postId);
    }
}

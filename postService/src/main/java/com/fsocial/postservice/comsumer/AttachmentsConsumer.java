package com.fsocial.postservice.comsumer;

import com.fsocial.postservice.dto.post.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttachmentsComsumer {

    @RabbitListener(queues = "${rabbitmq.queue.post.attachments.delete}")
    public void receiveAttachments(PostDTO postDTO){
        System.out.println("postId: attachments " + postDTO.getId());
    }
}

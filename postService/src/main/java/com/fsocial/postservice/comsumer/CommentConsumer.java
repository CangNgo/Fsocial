package com.fsocial.postservice.comsumer;

import com.fsocial.postservice.dto.comment.CommentDTO;
import com.fsocial.postservice.dto.post.PostDTO;
import com.fsocial.postservice.exception.AppCheckedException;
import com.fsocial.postservice.services.CommentService;
import com.fsocial.postservice.services.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Component
@RequiredArgsConstructor
public class CommentConsumer {

    CommentService commentService;

    @RabbitListener(queues = "${rabbitmq.queue.post.comment.delete}")
    public void receiveComment(String postId) throws AppCheckedException {
        commentService.deleteCommentByPostId(postId);
    }
}

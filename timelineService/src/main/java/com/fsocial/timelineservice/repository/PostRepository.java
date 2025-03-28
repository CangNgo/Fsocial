package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<PostResponse> findByContentTextContaining(String content);
    List<PostResponse> findByContentTextContainingIgnoreCase(String content);
    @Aggregation(pipeline = {
            "{$match: {_id: ?0}}",
            "{ $project: {countLikes : {$size: '$likes'}}}"
    })
    Integer countLikeByPost(String postId);

    boolean existsByIdAndLikes(String postId, String userId);
//
//    @Query(value = "{ '_id': { '$nin': ?0 } }", sort = "{ 'date_time': -1 }", limit = 10)
//    List<Post> findTop10(List<String> postIdViewed);

    List<Post> findByIdNotInOrUserIdInOrderByCreateDatetimeDesc(List<String> postIdViewed,List<String> userId,  Pageable pageable);
    List<Post> findByIdNotInOrderByCreateDatetimeDesc(List<String> postIdViewed, Pageable pageable);

}

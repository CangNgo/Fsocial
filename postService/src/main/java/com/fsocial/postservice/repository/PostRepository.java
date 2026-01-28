package com.fsocial.postservice.repository;

import com.fsocial.postservice.dto.post.PostResponse;
import com.fsocial.postservice.dto.post.PostStatisticsDTO;
import com.fsocial.postservice.dto.post.PostStatisticsLongDateDTO;
import com.fsocial.postservice.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    @Aggregation(pipeline = {
            "{$match: {_id: ?0}}",
            "{ $project: {countLikes : {$size: '$likes'}}}"
    })
    Integer countLikeByPost(String postId);
    boolean existsByIdAndLikes(String postId, String userId);
    // Query by owner.userId since Post has owner field, not userId directly
    List<Post> findByOwnerUserId(String userId);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'user_id': 1 }")
    Optional<String> findUserIdById(String id);

    // Methods from timelineService
    List<Post> findByContentTextContainingIgnoreCase(String content);
    List<Post> findByIdNotInOrderByCreateDatetimeDesc(List<String> postIdViewed, Pageable pageable);
    // Query by owner.userId since Post has owner field, not userId directly
    @Query("{ 'owner.user_id': { $in: ?0 }, '_id': { $nin: ?1 } }")
    List<Post> findByOwnerUserIdAndIdNotInOrderByCreateDatetimeDesc(List<String> userId, List<String> postId, Pageable pageable);

    @Aggregation(pipeline = {
            "{ '$match': { 'created_datetime': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { '_id': { '$hour': '$created_datetime' }, 'count': { '$sum': 1 } } }",
            "{ '$project': { 'hour': '$_id', 'count': 1, '_id': 0 } }"
    })
    List<PostStatisticsDTO> countByCreatedAtByHours(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(pipeline = {
            "{ '$match': { 'created_datetime': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { '_id': { '$dateTrunc': { 'date': '$created_datetime', 'unit': 'day' } }, 'count': { '$sum': 1 } } }",
            "{ '$project': { 'date': '$_id', 'count': 1, '_id': 0 } }",
            "{ '$sort': { 'date': 1 } }"
    })
    List<PostStatisticsLongDateDTO> countByDate(LocalDateTime startDate, LocalDateTime endDate);
    List<PostResponse> findByContentTextContaining(String content);

    List<Post> findByIdNotInOrOwnerUserIdInOrderByCreateDatetimeDesc(List<String> postIdViewed,List<String> userId,  Pageable pageable);

}

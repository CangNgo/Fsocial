package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsDTO;
import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsLongDayDTO;
import com.fsocial.timelineservice.dto.post.PostResponse;
import com.fsocial.timelineservice.dto.post.PostStatisticsDTO;
import com.fsocial.timelineservice.dto.post.PostStatisticsLongDateDTO;
import com.fsocial.timelineservice.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
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

    @Aggregation(pipeline = {
            "{ '$match': { 'created_datetime': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { '_id': { '$hour': '$created_datetime' }, 'count': { '$sum': 1 } } }",
            "{ '$project': { 'hour': '$_id', 'count': 1, '_id': 0 } }"
    })
    List<PostStatisticsDTO> countByCreatedAtByHours(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(pipeline = {
            "{ '$match': { 'created_datetime': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { '_id': { '$dateTrunc': { 'date': '$created_datetime', 'unit': 'day' } }, 'count': { '$sum': 1 } } }",
            "{ '$project': { 'date': '$_id', 'count': 1, '_id': 0 } }" +
            "{ '$sort': { 'date': 1 } }"
    })
    List<PostStatisticsLongDateDTO> countByDate(LocalDateTime startDate, LocalDateTime endDate);

}

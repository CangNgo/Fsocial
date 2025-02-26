package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Like;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface LikePostRepository extends MongoRepository<Like, String> {

    // Kiểm tra user đã like bài viết chưa
    boolean existsByPostIdAndUserId(String postId, String userId);

    // Thêm userId vào mảng userIds (nếu chưa tồn tại)
    @Modifying
    @Query("{ 'postId': ?0 }")
    @Update("{ '$addToSet': { 'userIds': ?1 } }")
    void addUserIdToPost(String postId, String userId);

    // Xóa userId khỏi mảng userIds (nếu tồn tại)
    @Modifying
    @Query("{ 'postId': ?0 }")
    @Update("{ '$pull': { 'userIds': ?1 } }")
    void removeUserIdFromPost(String postId, String userId);
}

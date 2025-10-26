package com.fsocial.profileservice.repository;

import com.fsocial.profileservice.entity.AccountProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountProfileRepository extends Neo4jRepository<AccountProfile, String> {
    Optional<AccountProfile> findByUserId(String userId);
    boolean existsByUserId(String userId);

    @Query("""
        MATCH (a:account_profile {user_id: $followerId})
        MATCH (b:account_profile {user_id: $followingId})
        MERGE (a)-[:FOLLOWS]->(b)
    """)
    void followUser(String followerId, String followingId);

    @Query("""
        MATCH (a:account_profile {user_id: $followerId})-[r:FOLLOWS]->(b:account_profile {user_id: $followingId}) 
        DELETE r
    """)
    void unfollowUser(String followerId, String followingId);

    @Query("""
        MATCH (a:account_profile {user_id: $userId})-[:FOLLOWS]->(b) 
        RETURN b
    """)
    List<AccountProfile> getFollowingUsers(String userId);

    @Query("""
              MATCH (a:account_profile {user_id: $userId})-[:FOLLOWS]->(b) 
        RETURN b.user_id
            """)
    List<String>  getListFollowingUsers(String userId);

    @Query("""
        MATCH (a)-[:FOLLOWS]->(b:account_profile {user_id: $userId}) 
        RETURN a
    """)
    List<AccountProfile> getFollowers(String userId);

    @Query("MATCH (a:AccountProfile {userId: $ownerId})-[:FOLLOWS]->(b:AccountProfile {userId: $userId}) RETURN COUNT(b) > 0")
    boolean isFollowing(@Param("ownerId") String ownerId, @Param("userId") String userId);

}

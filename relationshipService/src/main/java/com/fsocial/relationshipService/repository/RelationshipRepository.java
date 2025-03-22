package com.fsocial.relationshipService.repository;

import com.fsocial.relationshipService.entity.Relationship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends MongoRepository<Relationship, String> {
}

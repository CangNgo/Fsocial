package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.TermOfServices;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends MongoRepository<TermOfServices, String> {
}

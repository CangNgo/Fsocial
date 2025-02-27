package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.entity.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {
}

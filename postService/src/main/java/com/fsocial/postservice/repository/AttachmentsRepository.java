package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Attachments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentsRepository extends MongoRepository<Attachments, String> {

}

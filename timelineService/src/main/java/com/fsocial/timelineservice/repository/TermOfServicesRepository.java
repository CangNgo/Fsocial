package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.entity.TermOfServices;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermOfServicesRepository extends MongoRepository<TermOfServices,String> {
}

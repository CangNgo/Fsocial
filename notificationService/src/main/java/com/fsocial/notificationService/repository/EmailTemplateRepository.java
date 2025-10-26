package com.fsocial.notificationService.repository;

import com.fsocial.notificationService.entity.EmailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {
}

package com.fsocial.processorservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Bean
    @Primary
    @Qualifier("primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() {
        return new MongoTemplate(primaryMongoClient(), "primary_database");
    }

    @Bean
    @Qualifier("secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() {
        return new MongoTemplate(secondaryMongoClient(), "second_database");
    }

    @Bean
    public MongoClient primaryMongoClient() {
        return MongoClients.create("mongodb://root:root@localhost:27017/primary_database?authSource=admin");
    }

    @Bean
    public MongoClient secondaryMongoClient() {
        return MongoClients.create("mongodb://root:root@localhost:27017/second_database?authSource=admin");
    }
}

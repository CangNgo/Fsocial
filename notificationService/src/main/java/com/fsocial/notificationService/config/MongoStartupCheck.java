package com.fsocial.notificationService.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.mongodb.client.MongoClient;

@Component
public class MongoStartupCheck implements CommandLineRunner {

    private final MongoClient mongoClient;

    public MongoStartupCheck(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void run(String... args) {
        try {
            mongoClient.getDatabase("admin").runCommand(new org.bson.Document("ping", 1));
            System.out.println("✅ MongoDB is reachable at startup!");
        } catch (Exception e) {
            System.err.println("❌ Cannot connect to MongoDB: " + e.getMessage());
        }
    }
}


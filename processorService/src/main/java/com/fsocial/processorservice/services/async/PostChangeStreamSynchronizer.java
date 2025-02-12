package com.fsocial.processorservice.services.async;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PostChangeStreamSynchronizer {


    private final MongoClient primaryMongoClient;
    private final MongoTemplate sourceMongo;
    private final MongoTemplate targetMongo;

    public PostChangeStreamSynchronizer(
            @Qualifier("primaryMongoClient") MongoClient primaryMongoClient,
            @Qualifier("primaryMongoTemplate") MongoTemplate sourceMongo,
            @Qualifier("secondaryMongoTemplate") MongoTemplate targetMongo
    ) {
        this.primaryMongoClient = primaryMongoClient;
        this.sourceMongo = sourceMongo;
        this.targetMongo = targetMongo;
    }
    private ExecutorService executorService;
    private volatile boolean running = true;

    @PostConstruct
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
        startChangeStreamListener();
    }


    private void startChangeStreamListener() {
        executorService.submit(() -> {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    MongoCollection<Document> sourceCollection = primaryMongoClient
                            .getDatabase("primary_database")
                            .getCollection("users");

                    try (MongoCursor<ChangeStreamDocument<Document>> cursor = sourceCollection.watch(Arrays.asList(
                            Aggregates.sort(
                                    Filters.or(
                                            Filters.eq("operationType", "insert"),
                                            Filters.eq("operationType", "update"),
                                            Filters.eq("operationType", "delete")
                                    )
                            )
                    )).cursor()) {
                        while (running && cursor.hasNext()) {
                            ChangeStreamDocument<Document> changeEvent = cursor.next();
                            processChangeEvent(changeEvent);
                        }
                    }
                } catch (Exception e) {
                    log.error("Change stream error, reconnecting in 5 seconds", e);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    private void processChangeEvent(ChangeStreamDocument<Document> changeEvent) {
        try {
            switch (changeEvent.getOperationType()) {
                case INSERT -> handleInsert(changeEvent);
                case UPDATE -> handleUpdate(changeEvent);
                case DELETE -> handleDelete(changeEvent);
                default -> log.warn("Unsupported operation: {}", changeEvent.getOperationType());
            }
        } catch (Exception e) {
            log.error("Error processing change event", e);
        }
    }

    private void handleInsert(ChangeStreamDocument<Document> changeEvent) {
        Document fullDocument = changeEvent.getFullDocument();
        if (fullDocument != null) {
            targetMongo.insert(fullDocument, "users");
            log.info("Inserted document: {}", fullDocument.getObjectId("_id"));
        }
    }

    private void handleUpdate(ChangeStreamDocument<Document> changeEvent) {
        Document fullDocument = changeEvent.getFullDocument();
        BsonDocument documentKey = changeEvent.getDocumentKey();

        if (fullDocument != null && documentKey != null) {
            Object id = documentKey.get("_id");
            targetMongo.save(fullDocument, "users");
            log.info("Updated document: {}", id);
        }
    }

    private void handleDelete(ChangeStreamDocument<Document> changeEvent) {
        BsonDocument documentKey = changeEvent.getDocumentKey();

        if (documentKey != null) {
            Object id = documentKey.get("_id");
            targetMongo.remove(
                    Query.query(Criteria.where("_id").is(id)),
                    "users"
            );
            log.info("Deleted document: {}", id);
        }
    }

    public void initialSync() {
        List<Document> sourceUsers = sourceMongo.findAll(Document.class, "users");

        targetMongo.dropCollection("users");

        if (!sourceUsers.isEmpty()) {
            targetMongo.insert(sourceUsers, "users");
            log.info("Initial sync completed: {} documents", sourceUsers.size());
        }
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
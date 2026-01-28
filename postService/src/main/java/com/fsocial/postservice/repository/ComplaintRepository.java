package com.fsocial.postservice.repository;

import com.fsocial.postservice.entity.Complaint;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {
        // Methods from timelineService
        @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
                        "{ '$match': { 'dateTime': { '$gte': ?0, '$lte': ?1 } } }",
                        "{ '$group': { '_id': { '$hour': '$created_datetime' }, 'count': { '$sum': 1 } } }",
                        "{ '$project': { 'hour': '$_id', 'count': 1, '_id': 0 } }"
        })
        List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsDTO> countByCreatedAtByHours(
                        java.time.LocalDateTime startDay, java.time.LocalDateTime endDay);

        @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
                        "{ '$match': { 'dateTime': { '$gte': ?0, '$lte': ?1 } } }",
                        "{ '$group': { '_id': { '$dateTrunc': { 'date': '$created_datetime', 'unit': 'day' } }, 'count': { '$sum': 1 } } }",
                        "{ '$project': { 'date': '$_id', 'count': 1, '_id': 0 } }",
                        "{ '$sort': { 'date': 1 } }"
        })
        List<com.fsocial.postservice.dto.complaint.ComplaintStatisticsLongDayDTO> countByDate(
                        java.time.LocalDateTime startDay, java.time.LocalDateTime endDay);
}

package com.fsocial.timelineservice.repository;

import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsDTO;
import com.fsocial.timelineservice.dto.complaint.ComplaintStatisticsLongDayDTO;
import com.fsocial.timelineservice.entity.Complaint;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.Document;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {

    @Aggregation(pipeline = {
            "{ '$match': { 'dateTime': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { '_id': { '$hour': '$created_datetime' }, 'count': { '$sum': 1 } } }",
            "{ '$project': { 'hour': '$_id', 'count': 1, '_id': 0 } }"
    })
    List<ComplaintStatisticsDTO> countByCreatedAtByHours(LocalDateTime startDay, LocalDateTime endDay);

    @Aggregation(pipeline = {
            "{ '$match': { 'dateTime': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { '_id': { '$dateTrunc': { 'date': '$created_datetime', 'unit': 'day' } }, 'count': { '$sum': 1 } } }",
            "{ '$project': { 'date': '$_id', 'count': 1, '_id': 0 } }",
            "{ '$sort': { 'date': 1 } }"
    })
    List<ComplaintStatisticsLongDayDTO> countByDate(LocalDateTime startDay, LocalDateTime endDay);
}

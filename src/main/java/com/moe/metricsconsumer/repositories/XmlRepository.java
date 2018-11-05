package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.ExerciseDocument;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XmlRepository extends MongoRepository<ExerciseDocument, String> {

}




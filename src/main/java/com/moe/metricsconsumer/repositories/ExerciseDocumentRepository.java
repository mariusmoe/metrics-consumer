package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.ExerciseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseDocumentRepository extends MongoRepository<ExerciseDocument, String> {



}

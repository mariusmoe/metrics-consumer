package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.ExerciseDocument;
import com.moe.metricsconsumer.models.FvConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FvConfigurationRepository extends MongoRepository<FvConfiguration, String> {


  public FvConfiguration findFirstByTaskId(String taskId);
}




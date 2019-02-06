package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.ExerciseDocument;
import com.moe.metricsconsumer.models.FvConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FvConfigurationRepository extends MongoRepository<FvConfiguration, String> {


  public Optional<FvConfiguration> findFirstByTaskId(String taskId);
}




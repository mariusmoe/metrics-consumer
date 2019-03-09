package com.moe.metricsconsumer.repositories;


import com.moe.metricsconsumer.models.ExSolution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExSolutionRepository extends MongoRepository<ExSolution, String> {

  public Optional<ExSolution> findFirstByExClassNameAndExTitle(String exClassName, String exTitle);

}

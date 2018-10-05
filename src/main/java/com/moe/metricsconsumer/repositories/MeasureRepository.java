package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MeasureRepository extends MongoRepository<MeasureSummary, String> {

  public MeasureSummary findFirstById(String id);
  public MeasureSummary getFirstByTaskName(String taskName);
  public List<MeasureSummary> findByTaskName(String taskName);

  /**
   * Returns a list of all tasknames and taskIds that the user has registered
   * @param userId  The userId of the user Should be derived from principal!
   * @return
   */
  @Query(value="{ 'userId' : ?0 }", fields="{ 'userId' : 1,'taskId' : 1, 'taskName' : 1}")
    List<MeasureSummary> findAllByUserId(String userId);


  public MeasureSummary findFirstByUserIdAndTaskId(String userId, String taskId);




}

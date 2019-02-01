package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.rewards.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, String> {

  public List<Achievement> findByTaskIdRefOrIsCumulative(String taskId, boolean cumulative);
  /**
   * Returns a list of all achievements without the binary data
   * @return achievements
   */
  @Query(value="{ }", fields="{ 'id' : 1," +
    "'threshold' : 1, " +
    "'isCumulative' : 1, " +
    "'taskIdRef' : 1, " +
    "'name' : 1," +
    "'description': 1}")
  List<Achievement> findAllWithoutBinary();






}

package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.rewards.UserAchievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface UserAchievementRepository extends MongoRepository<UserAchievement, String> {
  public List<UserAchievement> findByUserRef(String userRef);
  public List<UserAchievement> findAllById(String userId);

  public List<UserAchievement> findAllByUserRef(String userId);

/*  *//**
   * Returns a list of all tasknames and taskIds that the user has registered
   * @param userRef  The userId of the user Should be derived from principal!
   * @return
   *//*
  @Query(value="{ 'userRef' : ?0 }", fields="{ 'id' : 1," +
    "'userRef' : 1, " +
    "'achievementRef' : 1, " +
    "'achievementRef' : 1, " +
    "'achievementRef' : 1}")
  List<UserAchievement> findAllByUserRef(String userRef);*/
}

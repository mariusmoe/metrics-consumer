package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.rewards.UserAchievement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserAchievementRepository extends MongoRepository<UserAchievement, String> {
  public List<UserAchievement> findByUserRef(String userRef);
}

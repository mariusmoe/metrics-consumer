package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AchievementRepository extends MongoRepository<Achievement, String> {

}

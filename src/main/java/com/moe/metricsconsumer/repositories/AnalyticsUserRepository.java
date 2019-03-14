package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.AnalyticsUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalyticsUserRepository extends MongoRepository<AnalyticsUser, String> {

  Optional<AnalyticsUser> findByUserId(String userId);
}

package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {

  Users findByUsername(String username);
}

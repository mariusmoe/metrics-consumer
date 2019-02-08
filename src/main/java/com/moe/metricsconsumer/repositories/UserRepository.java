package com.moe.metricsconsumer.repositories;

import com.moe.metricsconsumer.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {

  Optional<Users> findByUsername(String username);
}

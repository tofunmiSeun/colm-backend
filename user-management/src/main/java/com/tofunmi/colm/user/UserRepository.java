package com.tofunmi.colm.user;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By tofunmi on 12/07/2022
 */

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
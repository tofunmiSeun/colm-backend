package com.tofunmi.mitri.usermanagement.profile;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created By tofunmi on 12/07/2022
 */

public interface ProfileRepository extends MongoRepository<Profile, String> {
    List<Profile> findByUserId(String userId);
    Boolean existsByUsername(String username);
}
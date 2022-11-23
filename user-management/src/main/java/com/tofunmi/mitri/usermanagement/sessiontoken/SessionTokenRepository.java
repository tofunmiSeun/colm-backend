package com.tofunmi.mitri.usermanagement.sessiontoken;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By tofunmi on 12/07/2022
 */

public interface SessionTokenRepository extends MongoRepository<SessionToken, String> {
}
package com.tofunmi.mitri.webservice.follows;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By tofunmi on 15/07/2022
 */
public interface FollowRepository extends MongoRepository<Follow, FollowId> {

}

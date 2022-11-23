package com.tofunmi.mitri.webservice.post;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By tofunmi on 15/07/2022
 */
public interface PostRepository extends MongoRepository<Post, String> {
}

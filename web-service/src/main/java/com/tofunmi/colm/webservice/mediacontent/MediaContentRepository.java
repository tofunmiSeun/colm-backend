package com.tofunmi.colm.webservice.mediacontent;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By tofunmi on 15/07/2022
 */
public interface MediaContentRepository extends MongoRepository<MediaContent, String> {
}

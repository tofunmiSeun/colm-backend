package com.tofunmi.mitri.webservice.interactions;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By tofunmi on 15/12/2022
 */
public interface InteractionRepository extends MongoRepository<Interaction, String> {
}

package com.tofunmi.colm.api.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created By tofunmi on 03/02/2023
 */
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findAllByParticipantsContainsOrderByLastActivityDateDesc(String participant);
}

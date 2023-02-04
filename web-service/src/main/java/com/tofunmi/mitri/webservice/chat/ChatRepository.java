package com.tofunmi.mitri.webservice.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findAllByParticipantsContainsSortByLastActivityDateDesc(String participant);
}

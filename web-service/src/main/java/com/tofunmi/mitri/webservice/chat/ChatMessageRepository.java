package com.tofunmi.mitri.webservice.chat;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
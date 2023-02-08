package com.tofunmi.mitri.webservice.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findAllByChatIdOrderBySentOnDesc(String chatId);
    List<ChatMessage> findAllByChatIdInOrderBySentOnDesc(List<String> chatIds);
}
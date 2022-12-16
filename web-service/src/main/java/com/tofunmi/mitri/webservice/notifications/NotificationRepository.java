package com.tofunmi.mitri.webservice.notifications;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created By tofunmi on 15/12/2022
 */
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByProfileId(String profileId);
}

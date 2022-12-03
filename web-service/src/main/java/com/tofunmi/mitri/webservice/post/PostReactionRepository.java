package com.tofunmi.mitri.webservice.post;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
public interface PostReactionRepository extends MongoRepository<PostReaction, String> {
    boolean existsByPostIdAndProfileId(String postId, String profileId);

    void deleteByPostIdAndProfileId(String postId, String profileId);

    List<PostReaction> findAllByProfileIdAndReaction(String profileId, Reaction reaction);
}

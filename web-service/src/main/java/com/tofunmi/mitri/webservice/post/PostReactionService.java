package com.tofunmi.mitri.webservice.post;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created By tofunmi on 15/07/2022
 */
@Service
public class PostReactionService {
    private final PostReactionRepository repository;
    private final PostRepository postRepository;

    public PostReactionService(PostReactionRepository repository, PostRepository postRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
    }

    public void react(String postId, String profileId, Reaction reaction) {
        validatePostReactionInputs(postId, reaction);
        if (repository.existsByPostIdAndProfileId(postId, profileId)) {
            return;
        }

        PostReaction postReaction = new PostReaction();
        postReaction.setPostId(postId);
        postReaction.setProfileId(profileId);
        postReaction.setReaction(reaction);

        repository.save(postReaction);
    }

    public void removeReaction(String postId, String profileId, Reaction reaction) {
        validatePostReactionInputs(postId, reaction);
        repository.deleteByPostIdAndProfileId(postId, profileId);
    }

    public Set<String> getIdsForLikedPosts(String profileId) {
        return repository.findAllByProfileIdAndReaction(profileId, Reaction.LIKE)
                .parallelStream()
                .map(PostReaction::getPostId)
                .collect(Collectors.toSet());
    }

    private void validatePostReactionInputs(String postId, Reaction reaction) {
        Assert.hasText(postId, "Post id is invalid");
        Assert.notNull(reaction, String.format("%s cannot be null", reaction));
        Assert.isTrue(postRepository.findById(postId).isPresent(), String.format("Could not find post for id %s", postId));
    }
}
package com.tofunmi.mitri.webservice.interactions;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.webservice.follows.FollowsService;
import com.tofunmi.mitri.webservice.post.PostReactionService;
import com.tofunmi.mitri.webservice.post.PostService;
import com.tofunmi.mitri.webservice.post.Reaction;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created By tofunmi on 15/12/2022
 */
@Service
public class InteractionService {
    private final InteractionRepository repository;
    private final ProfileService profileService;
    private final PostService postService;
    private final PostReactionService postReactionService;
    private final FollowsService followsService;

    public InteractionService(InteractionRepository repository, ProfileService profileService,
                              PostService postService, PostReactionService postReactionService,
                              FollowsService followsService) {
        this.repository = repository;
        this.profileService = profileService;
        this.postService = postService;
        this.postReactionService = postReactionService;
        this.followsService = followsService;
    }

    public void newFollow(String follower, String followed) {
        Interaction interaction = newInteraction(follower, followed, InteractionType.FOLLOW);
        Assert.isTrue(followsService.profileIsBeingFollowed(followed, follower),
                String.format("Followership %s->%s does not exist", follower, followed));
        repository.save(interaction);
    }

    public void newPostReply(String replyAuthor, String originalPostAuthor, String postId, String replyId) {
        Interaction interaction = newInteraction(replyAuthor, originalPostAuthor, InteractionType.POST_REPLY);
        postService.validatePostExistence(postId);
        postService.validatePostExistence(replyId);
        interaction.setPostId(postId);
        interaction.setReplyId(replyId);
        repository.save(interaction);
    }

    public void newPostReaction(String reactor, String originalPostAuthor, String postId, Reaction reaction) {
        Interaction interaction = newInteraction(reactor, originalPostAuthor, InteractionType.POST_REACTION);
        postService.validatePostExistence(postId);
        postReactionService.validatePostReactionExists(postId, reaction);
        interaction.setPostId(postId);
        interaction.setReaction(reaction);
        repository.save(interaction);
    }

    private Interaction newInteraction(String actor, String recipient,InteractionType type) {
        profileService.validateProfileExistence(actor);
        profileService.validateProfileExistence(recipient);
        return new Interaction(actor, recipient,type);
    }

    public void markAsNotified(String id) {
        Interaction interaction = repository.findById(id).orElseThrow();
        interaction.setRecipientHasBeenNotified(true);
        repository.save(interaction);
    }

    public Long countNewNotifications(String recipient) {
        Interaction interaction = new Interaction();
        interaction.setRecipient(recipient);
        interaction.setRecipientHasBeenNotified(false);
        return repository.count(Example.of(interaction));
    }
}
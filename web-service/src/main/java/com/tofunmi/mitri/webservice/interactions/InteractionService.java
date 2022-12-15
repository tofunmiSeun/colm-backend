package com.tofunmi.mitri.webservice.interactions;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.webservice.follows.ProfileFollowedEvent;
import com.tofunmi.mitri.webservice.follows.ProfileUnfollowedEvent;
import com.tofunmi.mitri.webservice.post.PostReactionService;
import com.tofunmi.mitri.webservice.post.PostReplyPublishedEvent;
import com.tofunmi.mitri.webservice.post.PostService;
import com.tofunmi.mitri.webservice.post.Reaction;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created By tofunmi on 15/12/2022
 */
@Service
public class InteractionService {
    private final InteractionRepository repository;
    private final ProfileService profileService;
    private final PostService postService;
    private final PostReactionService postReactionService;

    public InteractionService(InteractionRepository repository, ProfileService profileService,
                              PostService postService, PostReactionService postReactionService) {
        this.repository = repository;
        this.profileService = profileService;
        this.postService = postService;
        this.postReactionService = postReactionService;
    }

    @EventListener
    public void handleProfileFollowedEvent(ProfileFollowedEvent event) {
        String follower = event.getFollower();
        String followed = event.getFollowed();
        Interaction interaction = newInteraction(follower, followed, InteractionType.FOLLOW);
        repository.save(interaction);
    }

    @EventListener
    public void handleProfileUnfollowedEvent(ProfileUnfollowedEvent event) {
        String follower = event.getFollower();
        String followed = event.getFollowed();
        Example<Interaction> example = getExampleForNonNotifiedRecipient(follower, followed, InteractionType.FOLLOW);
        repository.deleteAll(repository.findAll(example));
    }

    @EventListener
    public void handlePostReplyPublishedEvent(PostReplyPublishedEvent event) {
        String replyId = event.getReplyId();
        String replyAuthor = event.getReplyAuthor();
        String originalPostId = event.getOriginalPostId();
        String originalPostAuthor = event.getOriginalPostAuthor();

        Interaction interaction = newInteraction(replyAuthor, originalPostAuthor, InteractionType.POST_REPLY);
        interaction.setPostId(originalPostId);
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

    private Interaction newInteraction(String actor, String recipient, InteractionType type) {
        profileService.validateProfileExistence(actor);
        profileService.validateProfileExistence(recipient);
        return new Interaction(actor, recipient, type);
    }

    private Example<Interaction> getExampleForNonNotifiedRecipient(String actor, String recipient, InteractionType type) {
        Interaction interactionExample = new Interaction();
        interactionExample.setActor(actor);
        interactionExample.setRecipient(recipient);
        interactionExample.setType(type);
        interactionExample.setRecipientHasBeenNotified(false);
        return Example.of(interactionExample);
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

    public List<InteractionViewModel> getNotificationsForRecipient(String profileId) {
        List<Interaction> interactions = repository.findAllByRecipient(profileId);
        List<String> actors = interactions.stream()
                .map(Interaction::getActor)
                .collect(Collectors.toList());

        Map<String, String> usernamesForActors = profileService.getUsernamesForIds(actors);

        return interactions.stream()
                .map(e -> new InteractionViewModel(e.getId(), e.getCreatedOn(),
                        e.getActor(), e.getRecipientHasBeenNotified(),
                        e.getPostId(), e.getReplyId(), getDescriptionTemplate(e, usernamesForActors)))
                .collect(Collectors.toList());
    }

    private String getDescriptionTemplate(Interaction e,  Map<String, String> usernamesForActors) {
        String usernameForActor = usernamesForActors.getOrDefault(e.getActor(), "unknown-user");
        switch (e.getType()) {
            case FOLLOW -> {
                return String.format("%s followed you", usernameForActor);
            }
            case POST_REPLY -> {
                return String.format("%s replied to your post", usernameForActor);
            }
            case POST_REACTION -> {
                return String.format("%s reacted to your post", usernameForActor);
            }
            default -> throw new IllegalArgumentException("Unknown interaction type " + e.getType());
        }
    }
}
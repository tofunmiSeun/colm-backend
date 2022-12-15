package com.tofunmi.mitri.webservice.interactions;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.webservice.follows.ProfileFollowedEvent;
import com.tofunmi.mitri.webservice.follows.ProfileUnfollowedEvent;
import com.tofunmi.mitri.webservice.post.*;
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

    public InteractionService(InteractionRepository repository, ProfileService profileService) {
        this.repository = repository;
        this.profileService = profileService;
    }

    @EventListener
    public void handleProfileFollowedEvent(ProfileFollowedEvent event) {
        String follower = event.getFollower();
        String followed = event.getFollowed();
        Interaction interaction = new Interaction(follower, followed, InteractionType.FOLLOW);
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

        Interaction interaction = new Interaction(replyAuthor, originalPostAuthor, InteractionType.POST_REPLY);
        interaction.setPostId(originalPostId);
        interaction.setReplyId(replyId);
        repository.save(interaction);
    }

    @EventListener
    public void handlePostLikedEvent(PostLikedEvent event) {
        String postLiker = event.getProfileThatLikedPost();
        String postId = event.getPostId();
        String originalPostAuthor = event.getOriginalPostAuthor();

        Interaction interaction = new Interaction(postLiker, originalPostAuthor, InteractionType.POST_REACTION);
        interaction.setPostId(postId);
        interaction.setReaction(Reaction.LIKE);
        repository.save(interaction);
    }

    @EventListener
    public void handlePostLikeRemovedEvent(PostLikeRemovedEvent event) {
        String postLiker = event.getProfileThatLikedPost();
        String postId = event.getPostId();
        String originalPostAuthor = event.getOriginalPostAuthor();

        Example<Interaction> example = getExampleForNonNotifiedRecipient(postLiker, originalPostAuthor, InteractionType.POST_REACTION);
        example.getProbe().setPostId(postId);
        example.getProbe().setReaction(Reaction.LIKE);
        repository.deleteAll(repository.findAll(example));
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
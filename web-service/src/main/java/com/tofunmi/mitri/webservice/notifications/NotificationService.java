package com.tofunmi.mitri.webservice.notifications;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.webservice.follows.ProfileFollowedEvent;
import com.tofunmi.mitri.webservice.follows.ProfileUnfollowedEvent;
import com.tofunmi.mitri.webservice.post.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By tofunmi on 15/12/2022
 */
@Service
public class NotificationService {
    private final NotificationRepository repository;
    private final ProfileService profileService;

    public NotificationService(NotificationRepository repository, ProfileService profileService) {
        this.repository = repository;
        this.profileService = profileService;
    }

    @EventListener
    public void handleProfileFollowedEvent(ProfileFollowedEvent event) {
        String follower = event.getFollower();
        String followed = event.getFollowed();
        Notification notification = new Notification(follower, followed, NotificationType.FOLLOW);
        repository.save(notification);
    }

    @EventListener
    public void handleProfileUnfollowedEvent(ProfileUnfollowedEvent event) {
        String follower = event.getFollower();
        String followed = event.getFollowed();
        Example<Notification> example = getExampleForNonNotifiedRecipient(follower, followed, NotificationType.FOLLOW);
        repository.deleteAll(repository.findAll(example));
    }

    @EventListener
    public void handlePostReplyPublishedEvent(PostReplyPublishedEvent event) {
        String replyId = event.getReplyId();
        String replyAuthor = event.getReplyAuthor();
        String originalPostId = event.getOriginalPostId();
        String originalPostAuthor = event.getOriginalPostAuthor();

        Notification notification = new Notification(replyAuthor, originalPostAuthor, NotificationType.POST_REPLY);
        notification.setPostId(originalPostId);
        notification.setReplyId(replyId);
        repository.save(notification);
    }

    @EventListener
    public void handlePostLikedEvent(PostLikedEvent event) {
        String postLiker = event.getProfileThatLikedPost();
        String postId = event.getPostId();
        String originalPostAuthor = event.getOriginalPostAuthor();

        Notification notification = new Notification(postLiker, originalPostAuthor, NotificationType.POST_REACTION);
        notification.setPostId(postId);
        notification.setReaction(Reaction.LIKE);
        repository.save(notification);
    }

    @EventListener
    public void handlePostLikeRemovedEvent(PostLikeRemovedEvent event) {
        String postLiker = event.getProfileThatLikedPost();
        String postId = event.getPostId();
        String originalPostAuthor = event.getOriginalPostAuthor();

        Example<Notification> example = getExampleForNonNotifiedRecipient(postLiker, originalPostAuthor, NotificationType.POST_REACTION);
        example.getProbe().setPostId(postId);
        example.getProbe().setReaction(Reaction.LIKE);
        repository.deleteAll(repository.findAll(example));
    }

    private Example<Notification> getExampleForNonNotifiedRecipient(String actor, String recipient, NotificationType type) {
        Notification notificationExample = new Notification();
        notificationExample.setActor(actor);
        notificationExample.setProfileId(recipient);
        notificationExample.setType(type);
        notificationExample.setRecipientHasBeenNotified(false);
        return Example.of(notificationExample);
    }

    public List<NotificationViewModel> getNotificationsForProfile(String profileId) {
        List<Notification> notifications = repository.findAllByProfileId(profileId, Sort.by("happenedOn").descending());
        List<String> actors = notifications.stream()
                .map(Notification::getActor)
                .collect(Collectors.toList());

        Map<String, String> usernamesForActors = profileService.getUsernamesForIds(actors);

        return notifications.stream()
                .map(e -> new NotificationViewModel(e.getId(), e.getHappenedOn().toEpochMilli(),
                        e.getActor(), usernamesForActors.getOrDefault(e.getActor(), "unknown-user"),
                        e.getType(), e.getRecipientHasBeenNotified(),
                        e.getPostId(), e.getReplyId(), e.getReaction()))
                .collect(Collectors.toList());
    }

    private String getDescriptionTemplate(Notification e, Map<String, String> usernamesForActors) {
        String usernameForActor = usernamesForActors.getOrDefault(e.getActor(), "unknown-user");
        switch (e.getType()) {
            case FOLLOW: {
                return String.format("%s followed you", usernameForActor);
            }
            case POST_REPLY: {
                return String.format("%s replied to your post", usernameForActor);
            }
            case POST_REACTION: {
                return String.format("%s reacted to your post", usernameForActor);
            }
            default: throw new IllegalArgumentException("Unknown interaction type " + e.getType());
        }
    }

    public void markAsNotified(String id, String currentlyLoggedInProfile) {
        Notification notification = repository.findById(id).orElseThrow();
        Assert.isTrue(Objects.equals(currentlyLoggedInProfile, notification.getProfileId()), "Only notifications belonging to a profile can be updated");
        notification.setRecipientHasBeenNotified(true);
        repository.save(notification);
    }

    public Long countNewNotifications(String recipient) {
        Notification notification = new Notification();
        notification.setProfileId(recipient);
        notification.setRecipientHasBeenNotified(false);
        return repository.count(Example.of(notification));
    }
}
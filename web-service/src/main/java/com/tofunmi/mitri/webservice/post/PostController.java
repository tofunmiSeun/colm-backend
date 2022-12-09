package com.tofunmi.mitri.webservice.post;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@RestController
@RequestMapping("post")
public class PostController {
    private final PostService postService;
    private final PostReactionService postReactionService;
    private final ProfileService profileService;

    public PostController(PostService postService, PostReactionService postReactionService,
                          ProfileService profileService) {
        this.postService = postService;
        this.postReactionService = postReactionService;
        this.profileService = profileService;
    }

    @PostMapping
    public void newPost(@RequestParam String profileId, @RequestBody CreatePostRequest request, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        postService.createPost(request, profileId);
    }

    @GetMapping
    public List<PostViewModel> getAllPosts(@RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return postService.getForProfile(profileId);
    }

    @PostMapping("{id}/like")
    public void likePost(@PathVariable String id, @RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        postReactionService.react(id, profileId, Reaction.LIKE);
    }

    @PostMapping("{id}/like/remove")
    public void removeLike(@PathVariable String id, @RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        postReactionService.removeReaction(id, profileId, Reaction.LIKE);
    }

    @PostMapping("{id}/reply")
    public void replyPost(@RequestParam String profileId, @PathVariable("id") String originalPostId,
                          @RequestBody CreatePostRequest request, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        postService.replyToPost(originalPostId, request, profileId);
    }
}

package com.tofunmi.mitri.webservice.post;

import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
@RestController
@RequestMapping("api/post")
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

    @PostMapping(value = "create", consumes = "multipart/form-data")
    public void createPost(@RequestParam String profileId, Principal principal,
                           @RequestParam(value = "files", required = false) MultipartFile[] files,
                           @RequestParam(value = "text", required = false) String text) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        postService.createPost(profileId, text, files);
    }

    @GetMapping("feed")
    public List<PostViewModel> getPostsForFeed(@RequestParam String profileId, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return postService.getForFeed(profileId);
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

    @PostMapping(value = "{id}/reply", consumes = "multipart/form-data")
    public void replyPost(@RequestParam String profileId, @PathVariable("id") String originalPostId,
                          Principal principal,
                          @RequestParam(value = "files", required = false) MultipartFile[] files,
                          @RequestParam(value = "text", required = false) String text) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        postService.replyToPost(originalPostId, profileId, text, files);
    }

    @GetMapping("{id}/replies")
    public List<PostViewModel> getReplies(@RequestParam String profileId, @PathVariable("id") String id, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        return postService.getReplies(id, profileId);
    }

    @DeleteMapping("{id}")
    public void delete(@RequestParam String profileId, @PathVariable("id") String id, Principal principal) {
        profileService.validateProfileBelongsToUser(profileId, principal.getName());
        postService.deletePost(id, profileId);
    }

    @GetMapping("by-profile/{profileId}")
    public List<PostViewModel> getPostsAuthoredByProfile(@PathVariable("profileId") String profileId,
                                                         @RequestParam("profileId") String loggedInUserProfileId,
                                                         Principal principal) {
        profileService.validateProfileBelongsToUser(loggedInUserProfileId, principal.getName());
        return postService.findForProfile(profileId, loggedInUserProfileId);
    }
}

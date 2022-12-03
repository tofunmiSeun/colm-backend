package com.tofunmi.mitri.usermanagement.profile;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By tofunmi on 15/07/2022
 */
@Service
public class ProfileService {
    private final ProfileRepository repository;

    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    public Boolean isUsernameAvailable(String username) {
        return !repository.existsByUsername(username.toLowerCase());
    }

    public String createProfile(String userId, String username) {
        Profile profile = new Profile(userId, username);
        return repository.save(profile).getId();
    }

    public ProfileOverview getProfile(String id) {
        Profile profile = repository.findById(id).orElseThrow();
        return mapToOverview(profile);
    }

    public List<ProfileOverview> findForUser(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToOverview)
                .collect(Collectors.toList());
    }

    private ProfileOverview mapToOverview(Profile profile) {
        ProfileOverview overview = new ProfileOverview();
        overview.setId(profile.getId());
        overview.setUsername(profile.getUsername());
        overview.setName(profile.getName());
        overview.setDescription(profile.getDescription());
        return overview;
    }

    public void validateProfileBelongsToUser(String id, String userId) {
        Assert.hasText(id, String.format("Invalid profile id %s", id));
        Assert.hasText(userId, String.format("Invalid user id %s", userId));
        Profile profile = repository.findById(id).orElseThrow();
        Assert.isTrue(Objects.equals(userId, profile.getUserId()), String.format("Profile %s does not belong to user %s", id, userId));
    }
}

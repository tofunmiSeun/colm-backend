package com.tofunmi.mitri.usermanagement.profile;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
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

    public ProfileViewModel getProfile(String id) {
        Profile profile = repository.findById(id).orElseThrow();
        return mapToViewModel(profile);
    }

    public List<ProfileViewModel> getProfiles(Set<String> ids) {
        List<ProfileViewModel> overviews = new ArrayList<>();
        repository.findAllById(ids).forEach(p -> overviews.add(mapToViewModel(p)));
        return overviews;
    }

    public List<ProfileViewModel> findForUser(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToViewModel)
                .collect(Collectors.toList());
    }

    public ProfileViewModel mapToViewModel(Profile profile) {
        ProfileViewModel overview = new ProfileViewModel();
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

    public void validateProfileExistence(String id) {
        boolean profileExists = repository.existsById(id);
        Assert.isTrue(profileExists, String.format("Profile with id %s does not exist", id));
    }

    public Map<String, String> getUsernamesForIds(Collection<String> ids) {
        Map<String, String> map = new HashMap<>();
        Iterable<Profile> profiles = repository.findAllById(ids);
        profiles.forEach(profile -> map.put(profile.getId(), profile.getUsername()));
        return map;
    }
}

package com.tofunmi.mitri.usermanagement;

import com.tofunmi.mitri.usermanagement.jwt.GoogleJwtValidator;
import com.tofunmi.mitri.usermanagement.jwt.ValidatedJwtTokenDetails;
import com.tofunmi.mitri.usermanagement.profile.ProfileOverview;
import com.tofunmi.mitri.usermanagement.profile.ProfileService;
import com.tofunmi.mitri.usermanagement.user.User;
import com.tofunmi.mitri.usermanagement.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created By tofunmi on 12/07/2022
 */

@Service
public class UserManagementService {
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final GoogleJwtValidator googleJwtValidator;

    public UserManagementService(UserRepository userRepository,
                                 ProfileService profileService,
                                 GoogleJwtValidator googleJwtValidator) {
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.googleJwtValidator = googleJwtValidator;
    }

    public UserSetupResponse setupUserWithGoogleToken(String token) {
        ValidatedJwtTokenDetails tokenDetails = googleJwtValidator.validateGoogleIssuedToken(token);
        User user;
        boolean isExistingUser = userRepository.existsByEmail(tokenDetails.getEmail());
        if (isExistingUser) {
            user = userRepository.findByEmail(tokenDetails.getEmail());
        } else {
            user = new User(tokenDetails.getEmail(), tokenDetails.getFullName());
            user = userRepository.save(user);
        }

        List<ProfileOverview> userProfiles = isExistingUser ? profileService.findForUser(user.getId()) : new ArrayList<>();
        return new UserSetupResponse(user.getId(), user.getEmail(), user.getName(), isExistingUser, userProfiles);
    }
}

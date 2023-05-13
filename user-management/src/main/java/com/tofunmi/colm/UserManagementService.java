package com.tofunmi.colm;

import com.tofunmi.colm.jwt.GoogleJwtValidator;
import com.tofunmi.colm.jwt.ValidatedJwtTokenDetails;
import com.tofunmi.colm.profile.ProfileViewModel;
import com.tofunmi.colm.profile.ProfileService;
import com.tofunmi.colm.user.User;
import com.tofunmi.colm.user.UserRepository;
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
            user = new User(tokenDetails.getEmail(), tokenDetails.getFullName(), tokenDetails.getFirstName());
            user = userRepository.save(user);
        }

        List<ProfileViewModel> userProfiles = isExistingUser ? profileService.findForUser(user.getId()) : new ArrayList<>();
        return new UserSetupResponse(user.getId(), user.getEmail(), user.getFirstName(), isExistingUser, userProfiles);
    }
}

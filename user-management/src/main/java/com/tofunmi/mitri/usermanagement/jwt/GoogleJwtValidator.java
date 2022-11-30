package com.tofunmi.mitri.usermanagement.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.stereotype.Component;

/**
 * Created By tofunmi on 12/07/2022
 */

@Component
public class GoogleJwtValidator {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleJwtValidator(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    public ValidatedJwtTokenDetails validateGoogleIssuedToken(String token) {
        try {
            GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(token);
            if (googleIdToken == null) {
                throw new IllegalStateException("Google JWT token validation failed");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String fullName = (String) payload.get("name");
            String firstName = (String) payload.get("given_name");
            return new ValidatedJwtTokenDetails(email, fullName, firstName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.tofunmi.colm;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created By tofunmi on 12/07/2022
 */

@Configuration
public class Config {

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(
            @Value("${google-web-client-id}") String googleWebClientId,
            @Value("${google-ios-client-id}") String googleIosClientId
    ) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(List.of(googleWebClientId, googleIosClientId))
                .build();
    }
}

package com.spring.security.app.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2ClientController {

    @GetMapping("/")
    @ResponseBody 
    public String home(
            @RegisteredOAuth2AuthorizedClient("mi-cliente-web")
            OAuth2AuthorizedClient authorizedClient) {

        OAuth2AccessToken accessToken =
                authorizedClient.getAccessToken();

        return """
                <h1>OAuth2 funcionando</h1>

                <p><strong>Cliente:</strong> %s</p>

                <p><strong>Scopes:</strong> %s</p>

                <p><strong>Access Token:</strong></p>

                <textarea rows="10" cols="100">%s</textarea>
                """.formatted(
                    authorizedClient
                        .getClientRegistration()
                        .getClientId(),

                    accessToken.getScopes(),

                    accessToken.getTokenValue()
                );
    }
}

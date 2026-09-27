package com.spring.security.app.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.http.MediaType;

@Configuration
public class AuthorizationServerConfig {

    // endpoints Oauth2
    // /oauth2/token    /oauth2/authorize   /oauth2/jwks
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {

        //OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http); // deprecated

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer(); // OAuth2AuthorizationServerConfigurer configura esta SecurityFilterChain como un Authorization Server

        http
            .securityMatcher(
                authorizationServerConfigurer.getEndpointsMatcher() // solo peticiones que correspondan a los endpoints del Authorization Server
            )
            .with(
                authorizationServerConfigurer,
                Customizer.withDefaults() // Utiliza la configuración predeterminada de Spring
            )
            .authorizeHttpRequests(authorize ->
                authorize.anyRequest().authenticated() // Cualquier petición que haya coincidido con los endpoints del Authorization Server debe estar autenticada
            )
            .exceptionHandling(exceptions ->
                exceptions.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            );

        return http.build();
    }

    // define el cliente, aplicación que solicita un token al Authorization Server
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {

        RegisteredClient registeredClient =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("mi-cliente") // identificador del cliente
                        //.clientSecret("{noop}secreto") // {noop} NO sera codificado
                        .clientSecret(passwordEncoder.encode("secreto")) // codificado
                        .clientAuthenticationMethod(
                                ClientAuthenticationMethod.CLIENT_SECRET_BASIC // las credenciales del cliente se envían mediante HTTP Basic
                        )
                        .authorizationGrantType(
                                AuthorizationGrantType.CLIENT_CREDENTIALS // permite que un cliente (aplicación) obtenga un token utilizando sus propias credenciales, sin representar a un usuario final.
                        )
                        .authorizationGrantType(
                            AuthorizationGrantType.AUTHORIZATION_CODE // permite la autorizacion por usuario y clave
                        )
                        .redirectUri(
                            "http://127.0.0.1:8080/login/oauth2/code/mi-cliente" // redireccion dsps de login
                        )
                        .scope("read")
                        .scope("write")
                        .build();

        RegisteredClient webClient =
            RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("mi-cliente-web")
                    .clientAuthenticationMethod(
                            ClientAuthenticationMethod.NONE
                    )
                    .authorizationGrantType(
                            AuthorizationGrantType.AUTHORIZATION_CODE // permite la autorizacion por usuario y clave
                    )
                    .redirectUri( 
                            //"http://127.0.0.1:8080/login/oauth2/code/mi-cliente-web" // redireccion dsps de login
                            "http://localhost:8080/login/oauth2/code/mi-cliente-web" // redireccion dsps de login
                    )
                    .scope("read")
                    .scope("write")
                    .build();
        
        RegisteredClient postmanClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("mi-cliente-postman")
                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.NONE
                )
                .authorizationGrantType(
                        AuthorizationGrantType.AUTHORIZATION_CODE
                )
                .redirectUri(
                        "https://oauth.pstmn.io/v1/callback"
                )
                .scope("read")
                .scope("write")
                .build();

        return new InMemoryRegisteredClientRepository(
                registeredClient,
                webClient,
                postmanClient
        );
    }


    // crea claves para firmar o verificar JWT
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {

        KeyPairGenerator keyPairGenerator =
                KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Verifica JWT
        RSAPublicKey publicKey =
                (RSAPublicKey) keyPair.getPublic();

        // Firma JWT
        RSAPrivateKey privateKey =
                (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return (selector, context) ->
                selector.select(jwkSet);
    }
    

    // Esto proporciona la configuración de los endpoints del Authorization Server
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}

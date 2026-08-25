package org.sanmarcux.samples.sakila.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * Issues short-lived HS256 tokens for the Sakila staff table.
 * <p>
 * Deliberately has no *Business interface/impl pair and returns no HATEOAS model,
 * unlike the resource controllers: this is not a Sakila domain resource, there is no
 * entity/DTO mapping, and there is nothing to link to.
 *
 * @author Cesardl
 */
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthRestController.class);

    private static final Duration TTL = Duration.ofMinutes(30);
    private static final String ISSUER = "sakila-jee";

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthRestController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    public record TokenRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
    }

    @PostMapping("/token")
    public TokenResponse token(@Valid @RequestBody final TokenRequest request) {
        LOG.info("Invoking Rest Service token");

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(TTL))
                .subject(authentication.getName())
                // Single coarse scope. Add a JwtAuthenticationConverter alongside the first
                // @PreAuthorize that needs finer granularity, not before.
                .claim("scope", "api")
                .build();

        // The "typ" header must be set explicitly: NimbusJwtEncoder omits it when no header
        // is supplied, and Spring Security 7's resource-server decoder rejects such a token
        // with "the given typ value needs to be one of [JWT]".
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", TTL.toSeconds());
    }

    /**
     * Without this the AuthenticationException escapes to ExceptionTranslationFilter,
     * which answers with a Bearer challenge on what is supposed to be the login endpoint.
     * <p>
     * The body is identical for every failure mode - unknown user, wrong password,
     * disabled account - so it cannot be used to enumerate usernames.
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> onAuthenticationFailure(AuthenticationException ex) {
        LOG.debug("Authentication failed", ex);
        return Map.of("error", "invalid_credentials");
    }
}

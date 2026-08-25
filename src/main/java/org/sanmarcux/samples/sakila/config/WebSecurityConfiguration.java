package org.sanmarcux.samples.sakila.config;

import org.sanmarcux.samples.sakila.dao.StaffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created on 29/05/2018.
 *
 * @author Cesardl
 */
@Configuration
public class WebSecurityConfiguration {

    private final SecretKey jwtKey;

    /**
     * Resolved from the JWT_SECRET environment variable via relaxed binding. There is
     * deliberately no default: application.properties is committed to git, so a
     * fallback literal would be a published signing key. Missing secret == no startup.
     */
    public WebSecurityConfiguration(@Value("${jwt.secret}") String secret) {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        Assert.isTrue(key.length >= 32, "jwt.secret must be at least 32 bytes (256 bits) for HS256");
        this.jwtKey = new SecretKeySpec(key, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return NimbusJwtEncoder.withSecretKey(jwtKey).algorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(jwtKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Stores the algorithm as a {bcrypt} prefix inside the hash, so migrating to
     * argon2/scrypt later is a rehash-on-login rather than a schema migration.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(StaffRepository staffRepository) {
        return username -> staffRepository
                .findByUsername(username)
                // staff.password is nullable in Sakila (e.g. 'Jon'). A staff row without a
                // password is not a login. Routing it through UsernameNotFoundException also
                // triggers mitigateAgainstTimingAttack(), so it stays indistinguishable from
                // an unknown username.
                .filter(staff -> StringUtils.hasText(staff.getPassword()))
                .map(staff -> User.builder()
                        .username(staff.getUsername())
                        .password(staff.getPassword())
                        .disabled(!staff.isActive())
                        .roles("USER")
                        .build())
                // Never echo the username back: DaoAuthenticationProvider hides this behind
                // BadCredentialsException by default, but do not rely on that alone.
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:http://localhost:5173}") List<String> allowedOrigins) {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(allowedOrigins);
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        cors.setMaxAge(3600L);
        // No allowCredentials: the token travels in the Authorization header, not a cookie.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    /**
     * Actuator stays open, on two assumptions worth stating because both are load-bearing:
     * management.server.port keeps actuator off the public 8181 connector, and
     * management.server.address pins it to 127.0.0.1. The network is the boundary here,
     * not this chain. Boot also exposes only /health over HTTP by default.
     * <p>
     * Removing either management.server.* property would publish these endpoints
     * unauthenticated on the API port. Add authorization here before doing that.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                // Safe only because this API is stateless and authenticates from a header
                // rather than an ambient cookie. If the token ever moves into a cookie,
                // CSRF protection has to come back in the same commit.
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/token").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .build();
    }
}

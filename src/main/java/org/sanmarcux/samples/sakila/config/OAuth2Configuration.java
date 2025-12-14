package org.sanmarcux.samples.sakila.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

/**
 * Created on 29/05/2018.
 *
 * @author Cesardl
 */
@Configuration
@EnableResourceServer
@EnableAuthorizationServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    private static final String APPLICATION_NAME = "sakila-jee";

    // This is required for password grants, which we specify below as one of the
    // {@literal authorizedGrantTypes()}.
    private final AuthenticationManagerBuilder authenticationManager;

    @Autowired
    public OAuth2Configuration(AuthenticationManagerBuilder authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // Workaround for https://github.com/spring-projects/spring-boot/issues/1801
        endpoints.authenticationManager(authentication -> authenticationManager.getOrBuild().authenticate(authentication));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("android-" + APPLICATION_NAME)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_USER")
                .scopes("write")
                .resourceIds(APPLICATION_NAME)
                .secret("sakila-jee");
    }
}

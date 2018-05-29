package org.sanmarcux.samples.sakila.config;

import org.sanmarcux.samples.sakila.dao.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created on 29/05/2018.
 *
 * @author Cesardl
 */
@Configuration
public class WebSecurityConfiguration {

    private StaffRepository staffRepository;

    @Autowired
    public WebSecurityConfiguration(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (username) -> staffRepository
                .findByUsername(username)
                .map(staff -> User.builder()
                        .username(staff.getUsername())
                        .password(staff.getPassword())
                        .authorities("USER", "write")
                        .build())
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("could not find the user '%s'", username)));
    }
}

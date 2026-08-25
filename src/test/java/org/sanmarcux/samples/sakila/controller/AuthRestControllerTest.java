package org.sanmarcux.samples.sakila.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.sanmarcux.samples.sakila.AbstractIntegrationTest;
import org.sanmarcux.samples.sakila.SakilaApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The one test that actually guards the security path. Deliberately does NOT use
 * {@code @WithMockUser} -- it exercises real credentials against the seeded staff table
 * and a real signed token.
 *
 * @author Cesardl
 */
@SpringBootTest(classes = SakilaApplication.class)
@AutoConfigureMockMvc
public class AuthRestControllerTest extends AbstractIntegrationTest {

    private static final String CREDENTIALS = "{\"username\":\"cesar\",\"password\":\"cesar\"}";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void issuesTokenAndAcceptsItOnAProtectedEndpoint() throws Exception {
        String body = mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREDENTIALS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andExpect(jsonPath("$.expiresIn", is(1800)))
                .andReturn().getResponse().getContentAsString();

        String token = JsonPath.read(body, "$.accessToken");

        mockMvc.perform(get("/actors?size=1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void rejectsProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/actors")).andExpect(status().isUnauthorized());
    }

    @Test
    public void rejectsGarbageToken() throws Exception {
        mockMvc.perform(get("/actors").header(HttpHeaders.AUTHORIZATION, "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void rejectsWrongPassword() throws Exception {
        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"cesar\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("invalid_credentials")));
    }

    @Test
    public void rejectsUnknownUserIdenticallyToWrongPassword() throws Exception {
        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"nobody\",\"password\":\"whatever\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("invalid_credentials")));
    }

    /**
     * 'Jon' exists in sakila-data.sql with a NULL password. That must be a plain 401,
     * not a 500 from passing null into User.builder().
     */
    @Test
    public void rejectsStaffWithNullPassword() throws Exception {
        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Jon\",\"password\":\"anything\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("invalid_credentials")));
    }

    /**
     * A CORS preflight carries no Authorization header. If this 401s, the Vue SPA breaks
     * in a way that is genuinely painful to debug.
     */
    @Test
    public void allowsCorsPreflightWithoutAuthentication() throws Exception {
        mockMvc.perform(options("/actors")
                        .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "http://localhost:5173"));
    }
}

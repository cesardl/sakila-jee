package org.sanmarcux.samples.sakila.controller;

import org.junit.jupiter.api.Test;
import org.sanmarcux.samples.sakila.AbstractIntegrationTest;
import org.sanmarcux.samples.sakila.SakilaApplication;
import org.sanmarcux.samples.sakila.dto.ActorDTO;
import org.sanmarcux.samples.sakila.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
@SpringBootTest(classes = SakilaApplication.class)
@AutoConfigureMockMvc
@WithMockUser // these assert resource behaviour, not authentication; see AuthRestControllerTest
public class ActorRestControllerTest extends AbstractIntegrationTest {

    private final String actorId = "100";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper mapper;

    @Test
    public void readActors() throws Exception {
        mockMvc.perform(get("/actors?size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].actorId", is(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("PENELOPE")))
                .andExpect(jsonPath("$.content[0].lastName", is("GUINESS")))
                .andExpect(jsonPath("$.content[1].actorId", is(2)))
                .andExpect(jsonPath("$.content[1].firstName", is("NICK")))
                .andExpect(jsonPath("$.content[1].lastName", is("WAHLBERG")));
    }

    @Test
    public void actorNotFound() throws Exception {
        mockMvc.perform(get("/actors/1002"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleFilm() throws Exception {
        mockMvc.perform(get("/actors/1/films/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId", is(1)))
                .andExpect(jsonPath("$.title", is("ACADEMY DINOSAUR")))
                .andExpect(jsonPath("$.releaseYear", is(2006)));
    }

    @Test
    public void actorDoNotParticipateInFilm() throws Exception {
        mockMvc.perform(get("/actors/" + actorId + "/films/10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void readFilms() throws Exception {
        mockMvc.perform(get("/actors/" + actorId + "/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(24)));
    }

    @Test
    public void createActorParticipationInFilm() throws Exception {
        this.mockMvc.perform(put("/actors/" + actorId + "/films/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new FilmDTO())))
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("/actors/" + actorId + "/films/3"))
                .andExpect(status().isOk());
    }

    /**
     * PATCH is a partial update. modify() used to map the payload onto a brand new Actor,
     * so any field the caller left out was nulled -- which wiped last_name and then failed
     * outright on the NOT NULL last_update. Creates and removes its own row so the shared
     * fixture actors keep their names.
     */
    @Test
    public void partialUpdateKeepsOmittedFields() throws Exception {
        ActorDTO payload = new ActorDTO();
        payload.setFirstName("PATCH");
        payload.setLastName("SUBJECT");

        String location = this.mockMvc.perform(post("/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        this.mockMvc.perform(patch(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"PATCHED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("PATCHED")))
                .andExpect(jsonPath("$.lastName", is("SUBJECT")));

        this.mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("SUBJECT")));

        this.mockMvc.perform(delete(location)).andExpect(status().isOk());
    }
}

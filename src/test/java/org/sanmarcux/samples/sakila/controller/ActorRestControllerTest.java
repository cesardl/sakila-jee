package org.sanmarcux.samples.sakila.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sanmarcux.samples.sakila.SakilaApplication;
import org.sanmarcux.samples.sakila.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SakilaApplication.class)
@AutoConfigureMockMvc
public class ActorRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    private String actorId = "100";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void readActors() throws Exception {
        mockMvc.perform(get("/actors/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].actorId", is(100)))
                .andExpect(jsonPath("$.content[0].firstName", is("SPENCER")))
                .andExpect(jsonPath("$.content[0].lastName", is("DEPP")))
                .andExpect(jsonPath("$.content[1].actorId", is(101)))
                .andExpect(jsonPath("$.content[1].firstName", is("SUSAN")))
                .andExpect(jsonPath("$.content[1].lastName", is("DAVIS")));
    }

    @Test
    public void actorNotFound() throws Exception {
        mockMvc.perform(get("/actors/102"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleFilm() throws Exception {
        mockMvc.perform(get("/actors/" + actorId + "/films/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
//                .andExpect(jsonPath("$.filmId", is(this.films.get(0).getFilmId())))
//                .andExpect(jsonPath("$.title", is(this.films.get(0).getTitle())))
//                .andExpect(jsonPath("$.description", is(this.films.get(0).getDescription())));
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
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
//                .andExpect(jsonPath("$[0].filmId", is(this.films.get(0).getFilmId())))
//                .andExpect(jsonPath("$[0].title", is(this.films.get(0).getTitle())))
//                .andExpect(jsonPath("$[0].description", is(this.films.get(0).getDescription())))
//                .andExpect(jsonPath("$[1].filmId", is(this.films.get(1).getFilmId())))
//                .andExpect(jsonPath("$[1].title", is(this.films.get(1).getTitle())))
//                .andExpect(jsonPath("$[1].description", is(this.films.get(1).getDescription())));
    }

    @Test
    public void createActorParticipationInFilm() throws Exception {
        this.mockMvc.perform(put("/actors/" + actorId + "/films/3")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(new FilmDTO())))
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("/actors/" + actorId + "/films/3"))
                .andExpect(status().isOk());
    }
}
package org.sanmarcux.samples.sboot.sakila.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sanmarcux.samples.sboot.sakila.SakilaApplication;
import org.sanmarcux.samples.sboot.sakila.dto.DTOFilm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasSize;
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
    public void actorNotFound() throws Exception {
        mockMvc.perform(get("/actors/101"))
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
//        DTOFilm film = new DTOFilm();
//        film.setTitle("Dummy film");
//        film.setDescription("Description of film created by integration test");
//        film.setRentalRate(BigDecimal.valueOf(1, 12));
//        film.setReplacementCost(BigDecimal.valueOf(33,12));

        this.mockMvc.perform(put("/actors/" + actorId + "/films/3")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(new DTOFilm())))
                .andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(delete("/actors/" + actorId + "/films/3"))
                .andExpect(status().isOk());
    }
}
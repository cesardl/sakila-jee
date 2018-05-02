package org.sanmarcux.samples.sboot.sakila.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sanmarcux.samples.sboot.sakila.SakilaApplication;
import org.sanmarcux.samples.sboot.sakila.dto.FilmDTO;
import org.sanmarcux.samples.sboot.sakila.dto.LanguageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SakilaApplication.class)
@AutoConfigureMockMvc
public class FilmRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void filmNotFound() throws Exception {
        mockMvc.perform(get("/films/101"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createFilm() throws Exception {
        this.mockMvc.perform(post("/films/")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(buildFilm())))
                .andExpect(status().isCreated());
    }

    @Test
    public void createFilmWithUnregisteredLanguage() throws Exception {
        FilmDTO film = buildFilm();
        film.getLanguage().setId(100);

        this.mockMvc.perform(post("/films/")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isNotFound());
    }

    private FilmDTO buildFilm() {
        LanguageDTO language = new LanguageDTO();
        language.setId(1);

        FilmDTO film = new FilmDTO();
        film.setTitle("Dummy film");
        film.setDescription("Description of film created by integration test");
        film.setRentalRate(BigDecimal.valueOf(1, 12));
        film.setReplacementCost(BigDecimal.valueOf(33, 12));
        film.setLanguage(language);
        return film;
    }
}
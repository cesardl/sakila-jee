package org.sanmarcux.samples.sakila.controller;

import org.junit.jupiter.api.Test;
import org.sanmarcux.samples.sakila.AbstractIntegrationTest;
import org.sanmarcux.samples.sakila.SakilaApplication;
import org.sanmarcux.samples.sakila.dao.model.Rating;
import org.sanmarcux.samples.sakila.dto.FilmDTO;
import org.sanmarcux.samples.sakila.dto.LanguageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Year;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
@SpringBootTest(classes = SakilaApplication.class)
@AutoConfigureMockMvc
@WithMockUser // these assert resource behaviour, not authentication; see AuthRestControllerTest
public class FilmRestControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper mapper;

    @Test
    public void readFilms() throws Exception {
        mockMvc.perform(get("/films?size=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].filmId", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("ACADEMY DINOSAUR")))
                .andExpect(jsonPath("$.content[0].language.name", is("English")))
                .andExpect(jsonPath("$.content[1].filmId", is(2)))
                .andExpect(jsonPath("$.content[1].title", is("ACE GOLDFINGER")))
                .andExpect(jsonPath("$.content[1].language.name", is("English")))
                .andExpect(jsonPath("$.content[2].filmId", is(3)))
                .andExpect(jsonPath("$.content[2].title", is("ADAPTATION HOLES")))
                .andExpect(jsonPath("$.content[2].language.name", is("English")));
    }

    @Test
    public void filmNotFound() throws Exception {
        mockMvc.perform(get("/films/2000")).andExpect(status().isNotFound());
    }

    /**
     * film_id is SMALLINT UNSIGNED (0-65535) but the entity mapped it as Short, which stops
     * at 32767, so an id above that threw converting the repository's Integer argument into
     * the identifier -- a 500 instead of a 404. 40000 sits in the gap.
     */
    @Test
    public void filmNotFoundAboveShortRange() throws Exception {
        mockMvc.perform(get("/films/40000")).andExpect(status().isNotFound());
        mockMvc.perform(get("/films/65535")).andExpect(status().isNotFound());
    }

    @Test
    public void readFilmData() throws Exception {
        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId", is(1)))
                .andExpect(jsonPath("$.title", is("ACADEMY DINOSAUR")))
                // readFilms above asserted this on the paged endpoint, which populated the
                // language by hand; the single-film path returned null until the shared
                // Film -> FilmDTO post-converter took over.
                .andExpect(jsonPath("$.language.name", is("English")));
    }

    @Test
    public void createFilm() throws Exception {
        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildFilm())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void createFilmWithUnregisteredLanguage() throws Exception {
        FilmDTO film = buildFilm();
        film.getLanguage().setId(100);

        this.mockMvc.perform(post("/films/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(film))).andExpect(status().isNotFound());
    }

    @Test
    public void createFilmWithSendingFilmId() throws Exception {
        FilmDTO film = buildFilm();
        film.setFilmId(1);

        this.mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(film))).andExpect(status().isBadRequest());
    }

    private FilmDTO buildFilm() {
        LanguageDTO language = new LanguageDTO();
        language.setId(1);

        FilmDTO film = new FilmDTO();
        film.setTitle("Dummy film");
        film.setDescription("Description of film created by integration test");
        film.setReleaseYear(Year.of(1999));
        film.setLanguage(language);
        film.setRentalRate(BigDecimal.valueOf(1, 12));
        film.setReplacementCost(BigDecimal.valueOf(33, 12));
        film.setRating(Rating.GENERAL_AUDIENCES);
        return film;
    }
}

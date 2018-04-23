package org.sanmarcux.samples.sboot.sakila.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sanmarcux.samples.sboot.sakila.SakilaApplication;
import org.sanmarcux.samples.sboot.sakila.dao.ActorRepository;
import org.sanmarcux.samples.sboot.sakila.dao.FilmRepository;
import org.sanmarcux.samples.sboot.sakila.dao.model.Actor;
import org.sanmarcux.samples.sboot.sakila.dao.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SakilaApplication.class)
@WebAppConfiguration
public class ActorRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String userName = "bdussault";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Actor actor;

    private List<Film> films = new ArrayList<>();

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        // Prepare data
        this.filmRepository.deleteAllInBatch();
        this.actorRepository.deleteAllInBatch();

//        this.actor = actorRepository.save(new Actor(userName, "last name", new Date()));
//        this.films.add(filmRepository.save(new Film(actor, "http://bookmark.com/1/" + userName, "A description")));
//        this.films.add(filmRepository.save(new Film(actor, "http://bookmark.com/2/" + userName, "A description")));
    }

    @Test
    public void actorNotFound() throws Exception {
        mockMvc.perform(get("/actors/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleFilm() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks/"
                + this.films.get(0).getFilmId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.films.get(0).getFilmId().intValue())))
                .andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$.description", is("A description")));
    }

    @Test
    public void readFilms() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.films.get(0).getFilmId().intValue())))
                .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$[0].description", is("A description")))
                .andExpect(jsonPath("$[1].id", is(this.films.get(1).getFilmId().intValue())))
                .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + userName)))
                .andExpect(jsonPath("$[1].description", is("A description")));
    }

    @Test
    public void createFilm() throws Exception {
//        String bookmarkJson = json(new Film(
//                this.actor, "http://spring.io", "a bookmark to the best resource for Spring news and information"));
//
//        this.mockMvc.perform(post("/" + userName + "/bookmarks")
//                .contentType(contentType)
//                .content(bookmarkJson))
//                .andExpect(status().isCreated());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
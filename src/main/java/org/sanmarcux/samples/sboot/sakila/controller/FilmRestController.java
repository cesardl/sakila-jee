package org.sanmarcux.samples.sboot.sakila.controller;

import org.sanmarcux.samples.sboot.sakila.business.FilmBusiness;
import org.sanmarcux.samples.sboot.sakila.dto.FilmDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Created on 29/04/2018.
 *
 * @author Cesardl
 */
@RestController
@RequestMapping("/films")
public class FilmRestController {

    private static final Logger LOG = LoggerFactory.getLogger(FilmRestController.class);

    private FilmBusiness filmBusiness;

    @Autowired
    public void setFilmBusiness(FilmBusiness filmBusiness) {
        this.filmBusiness = filmBusiness;
    }

    @GetMapping
    public List<FilmDTO> listFilms() {
        LOG.info("Invoking Rest Service listFilms");
        return filmBusiness.list();
    }

    @PostMapping
    public ResponseEntity<?> createFilm(@Valid @RequestBody final FilmDTO payload) {
        LOG.info("Invoking Rest Service createFilmParticipation");

        FilmDTO result = filmBusiness.create(payload);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{filmId}")
                .buildAndExpand(result.getFilmId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{filmId}")
    public FilmDTO getFilm(@PathVariable Short filmId) {
        LOG.info("Invoking Rest Service getFilm");
        return filmBusiness.get(filmId);
    }
}

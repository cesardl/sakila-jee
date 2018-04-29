package org.sanmarcux.samples.sboot.sakila.controller;

import org.sanmarcux.samples.sboot.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sboot.sakila.dto.DTOActor;
import org.sanmarcux.samples.sboot.sakila.dto.DTOFilm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@RestController
@RequestMapping("/actors")
public class ActorRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ActorRestController.class);

    private ActorBusiness actorBusiness;

    @Autowired
    public void setActorBusiness(ActorBusiness actorBusiness) {
        this.actorBusiness = actorBusiness;
    }

    /**
     * List of actors obtained from database.
     *
     * @return actors
     */
    @GetMapping
    public List<DTOActor> listActors() {
        LOG.info("Invoking Rest Service listActors");
        return actorBusiness.list();
    }

    /**
     * Create a new actor and save it in the database.
     *
     * @param payload request body
     * @return a created actor
     */
    @PostMapping
    public ResponseEntity<?> createActor(@RequestBody final DTOActor payload) {
        LOG.info("Invoking Rest Service createActor");

        DTOActor result = actorBusiness.create(payload);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{actorId}")
                .buildAndExpand(result.getActorId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Read an actor by id from the database.
     *
     * @param actorId actor resource id
     * @return an actor
     */
    @GetMapping("/{actorId}")
    public DTOActor getActor(@PathVariable Short actorId) {
        LOG.info("Invoking Rest Service getActor");
        return actorBusiness.get(actorId);
    }

    /**
     * Update an actor record and save it in the database.
     *
     * @param actorId actor resource id
     * @param payload request body
     * @return a modified actor
     */
    @PutMapping("/{actorId}")
    public DTOActor modifyActor(@PathVariable final Short actorId, @RequestBody final DTOActor payload) {
        LOG.info("Invoking Rest Service modifyActor");
        return actorBusiness.modify(actorId, payload);
    }

    /**
     * Delete an actor from the database.
     *
     * @param actorId actor resource id
     */
    @DeleteMapping("/{actorId}")
    public ResponseEntity<?> deleteActor(@PathVariable final Short actorId) {
        LOG.info("Invoking Rest Service deleteActor");
        actorBusiness.delete(actorId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{actorId}/films")
    public List<DTOFilm> listActorFilms(@PathVariable final Short actorId) {
        LOG.info("Invoking Rest Service listActorFilms");
        return actorBusiness.listFilms(actorId);
    }

    @PostMapping("/{actorId}/films")
    public ResponseEntity<?> createActorFilm(@PathVariable final Short actorId, @RequestBody final DTOFilm payload) {
        LOG.info("Invoking Rest Service createActorFilm");

        DTOFilm result = actorBusiness.createFilm(actorId, payload);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{filmId}")
                .buildAndExpand(result.getFilmId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{actorId}/films/{filmId}")
    public DTOFilm getActorFilm(@PathVariable final Short actorId, @PathVariable final Short filmId) {
        LOG.info("Invoking Rest Service getActorFilm");

        return actorBusiness.getFilm(actorId, filmId);
    }

    @PutMapping("/{actorId}/films/{filmId}")
    public void modifyActorFilm(@PathVariable final Short actorId, @PathVariable final Short filmId, @RequestBody final DTOFilm payload) {
        LOG.info("Invoking Rest Service modifyActorFilm");
    }

    @DeleteMapping("/{actorId}/films/{filmId}")
    public ResponseEntity<?> deleteActorFilm(@PathVariable final Short actorId, @PathVariable final Short filmId) {
        LOG.info("Invoking Rest Service deleteActorFilm");
        actorBusiness.deleteFilm(actorId, filmId);

        return ResponseEntity.ok().build();
    }
}

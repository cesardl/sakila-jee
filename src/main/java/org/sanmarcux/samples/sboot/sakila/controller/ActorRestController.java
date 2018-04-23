package org.sanmarcux.samples.sboot.sakila.controller;

import org.sanmarcux.samples.sboot.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sboot.sakila.entities.DTOIntActor;
import org.sanmarcux.samples.sboot.sakila.entities.DTOIntFilm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @RequestMapping(method = RequestMethod.GET)
    public List<DTOIntActor> listActors() {
        LOG.info("Invoking Rest Service listActors");
        return actorBusiness.list();
    }

    /**
     * Create a new actor and save it in the database.
     *
     * @param payload request body
     * @return a created actor
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createActor(@RequestBody final DTOIntActor payload) {
        LOG.info("Invoking Rest Service createActor");

        DTOIntActor result = actorBusiness.create(payload);

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
    @RequestMapping(path = "/{actorId}", method = RequestMethod.GET)
    public DTOIntActor getActor(@PathVariable Short actorId) {
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
    @RequestMapping(path = "/{actorId}", method = RequestMethod.PUT)
    public DTOIntActor modifyActor(@PathVariable final Short actorId, @RequestBody final DTOIntActor payload) {
        LOG.info("Invoking Rest Service modifyActor");
        return actorBusiness.modify(actorId, payload);
    }

    /**
     * Delete an actor from the database.
     *
     * @param actorId actor resource id
     */
    @RequestMapping(path = "/{actorId}", method = RequestMethod.DELETE)
    public void deleteActor(@PathVariable final Short actorId) {
        LOG.info("Invoking Rest Service deleteActor");
        actorBusiness.delete(actorId);
    }

    @RequestMapping(path = "/{actorId}/films", method = RequestMethod.GET)
    public List<DTOIntFilm> listActorFilms(@PathVariable final Short actorId) {
        LOG.info("Invoking Rest Service listActorFilms");
        return null;
    }

    @RequestMapping(path = "/{actorId}/films", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void createActorFilm(@PathVariable final Short actorId, @RequestBody final DTOIntFilm payload) {
        LOG.info("Invoking Rest Service createActorFilm");
    }

    @RequestMapping(path = "/{actorId}/films/{filmId}", method = RequestMethod.GET)
    public void getActorFilm(@PathVariable final Short actorId, @PathVariable final Short filmId) {
        LOG.info("Invoking Rest Service getActorFilm");
    }

    @RequestMapping(path = "/{actorId}/films/{filmId}", method = RequestMethod.PUT)
    public void modifyActorFilm(@PathVariable final Short actorId, @PathVariable final Short filmId, @RequestBody final DTOIntFilm payload) {
        LOG.info("Invoking Rest Service modifyActorFilm");
    }

    @RequestMapping(path = "/{actorId}/films/{filmId}", method = RequestMethod.DELETE)
    public void deleteActorFilm(@PathVariable final Short actorId, @PathVariable final Short filmId) {
        LOG.info("Invoking Rest Service deleteActorFilm");
    }
}

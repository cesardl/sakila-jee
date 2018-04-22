package org.sanmarcux.samples.sboot.sakila.controller;

import org.sanmarcux.samples.sboot.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sboot.sakila.entities.DTOIntActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@RestController
@RequestMapping("/actors")
public class ActorController {

    private static final Logger LOG = LoggerFactory.getLogger(ActorController.class);

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
    @RequestMapping
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
    public DTOIntActor createActor(final DTOIntActor payload) {
        LOG.info("Invoking Rest Service createActor");
        return actorBusiness.create(payload);
    }

    /**
     * Read an actor by id from the database.
     *
     * @param actorId actor resource id
     * @return an actor
     */
    @RequestMapping("/{actorId}")
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
    @RequestMapping(method = RequestMethod.PUT, path = "/{actorId}")
    public DTOIntActor modifyActor(@PathVariable final Short actorId, final DTOIntActor payload) {
        LOG.info("Invoking Rest Service modifyActor");
        return actorBusiness.modify(actorId, payload);
    }

    /**
     * Delete an actor from the database.
     *
     * @param actorId actor resource id
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{actorId}")
    public void deleteActor(@PathVariable final Short actorId) {
        LOG.info("Invoking Rest Service deleteActor");
        actorBusiness.delete(actorId);
    }
}

package org.sanmarcux.samples.sboot.sakila.controller;

import org.sanmarcux.samples.sboot.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sboot.sakila.entities.DTOIntActor;
import org.sanmarcux.samples.sboot.sakila.exceptions.ActorNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public List<DTOIntActor> list() {
        List<DTOIntActor> actors = actorBusiness.all();
        LOG.info("Invoking {} actors", actors.size());
        return actors;
    }

    /**
     * Create a new actor and save it in the database.
     *
     * @param actor
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DTOIntActor create(final DTOIntActor actor) {
        return actorBusiness.save(actor);
    }

    /**
     * Read an actor by id from the database.
     *
     * @param actorId actor resource id
     * @return
     */
    @RequestMapping("/{actorId}")
    public DTOIntActor read(@PathVariable Short actorId) {
        Optional<DTOIntActor> optional = actorBusiness.findById(actorId);

        return optional.orElseThrow(() -> new ActorNotFoundException(actorId));
    }

    /**
     * Update an actor record and save it in the database.
     *
     * @param actorId actor resource id
     * @param payload
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{actorId}")
    public DTOIntActor update(@PathVariable final Short actorId, final DTOIntActor payload) {
        Optional<DTOIntActor> actorOptional = actorBusiness.findById(actorId);

        DTOIntActor actor = actorOptional.orElseThrow(() -> new ActorNotFoundException(actorId));
        actor.setFirstName(payload.getFirstName());
        actor.setLastName(payload.getLastName());

        return actorBusiness.save(actor);
    }

    /**
     * Delete an actor from the database.
     *
     * @param actorId actor resource id
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{actorId}")
    public void delete(@PathVariable final Short actorId) {
        actorBusiness.deleteById(actorId);
    }
}

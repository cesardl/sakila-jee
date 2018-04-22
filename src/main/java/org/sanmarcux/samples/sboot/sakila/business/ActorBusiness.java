package org.sanmarcux.samples.sboot.sakila.business;

import org.sanmarcux.samples.sboot.sakila.entities.DTOIntActor;

import java.util.List;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
public interface ActorBusiness {

    List<DTOIntActor> list();

    DTOIntActor create(DTOIntActor actor);

    DTOIntActor modify(Short actorId, DTOIntActor payload);

    DTOIntActor get(Short actorId);

    void delete(Short actorId);
}

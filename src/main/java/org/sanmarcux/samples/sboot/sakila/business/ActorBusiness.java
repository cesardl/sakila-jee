package org.sanmarcux.samples.sboot.sakila.business;

import org.sanmarcux.samples.sboot.sakila.entities.DTOIntActor;

import java.util.List;
import java.util.Optional;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
public interface ActorBusiness {

    List<DTOIntActor> all();

    DTOIntActor save(DTOIntActor actor);

    Optional<DTOIntActor> findById(Short actorId);

    void deleteById(Short actorId);
}

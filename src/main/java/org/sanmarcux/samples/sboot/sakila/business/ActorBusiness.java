package org.sanmarcux.samples.sboot.sakila.business;

import org.sanmarcux.samples.sboot.sakila.dto.DTOActor;
import org.sanmarcux.samples.sboot.sakila.dto.DTOFilm;

import java.util.List;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
public interface ActorBusiness {

    List<DTOActor> list();

    DTOActor create(DTOActor payload);

    DTOActor modify(Short actorId, DTOActor payload);

    DTOActor get(Short actorId);

    void delete(Short actorId);

    List<DTOFilm> listFilms(Short actorId);

    DTOFilm createFilm(Short actorId, DTOFilm payload);

    DTOFilm getFilm(Short actorId, Short filmId);

    void deleteFilm(Short actorId, Short filmId);
}

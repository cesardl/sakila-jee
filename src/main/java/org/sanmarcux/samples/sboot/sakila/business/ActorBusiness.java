package org.sanmarcux.samples.sboot.sakila.business;

import org.sanmarcux.samples.sboot.sakila.dto.ActorDTO;
import org.sanmarcux.samples.sboot.sakila.dto.FilmDTO;

import java.util.List;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
public interface ActorBusiness {

    List<ActorDTO> list();

    ActorDTO create(ActorDTO payload);

    ActorDTO modify(Short actorId, ActorDTO payload);

    ActorDTO get(Short actorId);

    void delete(Short actorId);

    void createFilmParticipation(Short actorId, Short payload);

    FilmDTO getFilm(Short actorId, Short filmId);

    void deleteFilm(Short actorId, Short filmId);
}

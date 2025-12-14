package org.sanmarcux.samples.sakila.business;

import org.sanmarcux.samples.sakila.dto.ActorDTO;
import org.sanmarcux.samples.sakila.dto.FilmDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
public interface ActorBusiness {

    Page<ActorDTO> list(Pageable pageable);

    ActorDTO create(ActorDTO payload);

    ActorDTO modify(Integer actorId, ActorDTO payload);

    ActorDTO get(Integer actorId);

    void delete(Integer actorId);

    void createFilmParticipation(Integer actorId, Integer filmId);

    FilmDTO getFilm(Integer actorId, Integer filmId);

    void deleteFilm(Integer actorId, Integer filmId);
}

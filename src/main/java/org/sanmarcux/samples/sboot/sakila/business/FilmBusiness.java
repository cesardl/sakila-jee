package org.sanmarcux.samples.sboot.sakila.business;

import org.sanmarcux.samples.sboot.sakila.dto.DTOFilm;

import java.util.List;

/**
 * Created on 29/04/2018.
 *
 * @author Cesardl
 */
public interface FilmBusiness {

    List<DTOFilm> list();

    DTOFilm create(DTOFilm payload);

    List<DTOFilm> findFilmsByActor(Short actorId);
}

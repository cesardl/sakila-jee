package org.sanmarcux.samples.sboot.sakila.business;

import org.sanmarcux.samples.sboot.sakila.dto.FilmDTO;

import java.util.List;

/**
 * Created on 29/04/2018.
 *
 * @author Cesardl
 */
public interface FilmBusiness {

    List<FilmDTO> list();

    FilmDTO create(FilmDTO payload);

    List<FilmDTO> findFilmsByActor(Short actorId);

    FilmDTO get(Short filmId);
}

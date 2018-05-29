package org.sanmarcux.samples.sakila.business;

import org.sanmarcux.samples.sakila.dto.FilmDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created on 29/04/2018.
 *
 * @author Cesardl
 */
public interface FilmBusiness {

    Page<FilmDTO> list(Pageable pageable);

    FilmDTO create(FilmDTO payload);

    List<FilmDTO> findFilmsByActor(Short actorId);

    FilmDTO get(Short filmId);
}

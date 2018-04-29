package org.sanmarcux.samples.sboot.sakila.dao;

import org.sanmarcux.samples.sboot.sakila.dao.model.FilmActor;
import org.sanmarcux.samples.sboot.sakila.dao.model.FilmActorId;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 28/04/2018.
 *
 * @author Cesardl
 */
public interface FilmActorRepository extends CrudRepository<FilmActor, FilmActorId> {
}

package org.sanmarcux.samples.sboot.sakila.dao;

import org.sanmarcux.samples.sboot.sakila.dao.model.FilmActor;
import org.sanmarcux.samples.sboot.sakila.dao.model.FilmActorId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 28/04/2018.
 *
 * @author Cesardl
 */
@Transactional
public interface FilmActorRepository extends CrudRepository<FilmActor, FilmActorId> {
}

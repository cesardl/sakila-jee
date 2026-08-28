package org.sanmarcux.samples.sakila.dao;

import org.sanmarcux.samples.sakila.dao.model.FilmActor;
import org.sanmarcux.samples.sakila.dao.model.FilmActorId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created on 28/04/2018.
 *
 * @author Cesardl
 */
@Transactional
public interface FilmActorRepository extends CrudRepository<FilmActor, FilmActorId> {

    /**
     * FilmActor.film is LAZY and spring.jpa.open-in-view is false, so the session is gone by
     * the time the business layer maps the film. Fetch it up front rather than relying on an
     * EAGER association that every other read of this entity would also pay for.
     */
    @Query("select fa from FilmActor fa join fetch fa.film f join fetch f.languageByLanguageId where fa.id = :id")
    Optional<FilmActor> findByIdFetchingFilm(@Param("id") FilmActorId id);
}

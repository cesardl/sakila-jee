package org.sanmarcux.samples.sboot.sakila.dao;

import org.sanmarcux.samples.sboot.sakila.dao.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
public interface FilmRepository extends JpaRepository<Film, Short> {

    @Query(value = "select * from film f inner join film_actor fa on f.film_id = fa.film_id where fa.actor_id = :actorId", nativeQuery = true)
    List<Film> findAllByActor(@Param("actorId") Short actorId);
}

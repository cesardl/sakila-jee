package org.sanmarcux.samples.sakila.dao;

import org.sanmarcux.samples.sakila.dao.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
@Transactional
public interface FilmRepository extends JpaRepository<Film, Integer> {

    /**
     * FilmDTO always carries the language, and Film.languageByLanguageId is EAGER, but on a
     * paginated query Hibernate resolves that association with a follow-up select per
     * distinct language on the page rather than joining it. The graph makes it a join.
     */
    @Override
    @EntityGraph(attributePaths = "languageByLanguageId")
    Page<Film> findAll(Pageable pageable);

    @Query(value = "select f.* from film f inner join film_actor fa on f.film_id = fa.film_id where fa.actor_id = :actorId", nativeQuery = true)
    List<Film> findAllByActor(@Param("actorId") Integer actorId);
}

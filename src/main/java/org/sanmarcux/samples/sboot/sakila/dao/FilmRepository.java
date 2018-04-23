package org.sanmarcux.samples.sboot.sakila.dao;

import org.sanmarcux.samples.sboot.sakila.dao.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
public interface FilmRepository extends JpaRepository<Film, Short> {
}

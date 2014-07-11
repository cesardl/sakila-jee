package pe.com.hiper.sample.sakila.shared.dao.model.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import pe.com.hiper.sample.sakila.shared.dao.SakilaDaoSupport;
import pe.com.hiper.sample.sakila.shared.dao.model.FilmDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Film;

/**
 * 
 * @author pdiaz
 * 
 */
@Repository("filmDao")
public class FilmDaoImpl extends SakilaDaoSupport implements FilmDao {

	public List<Film> all() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void delete(Short id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Film get(Short id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Boolean save(Film actor) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void update(Film actor) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Film> get(Actor actor) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}

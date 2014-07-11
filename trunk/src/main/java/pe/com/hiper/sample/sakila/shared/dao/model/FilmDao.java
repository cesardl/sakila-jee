package pe.com.hiper.sample.sakila.shared.dao.model;

import java.util.List;

import pe.com.hiper.sample.sakila.shared.dao.SakilaDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Film;

/**
 * 
 * @author pdiaz
 * 
 */
public interface FilmDao extends SakilaDao<Film> {

	public List<Film> get(Actor actor);
}

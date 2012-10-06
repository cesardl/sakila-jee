/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.shared.dao.model.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import pe.com.hiper.sakila.shared.dao.model.FilmDao;
import pe.com.hiper.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sakila.shared.domain.mapping.Film;

/**
 * 
 * @author pdiaz
 */
@Repository("filmDao")
public class FilmDaoImpl extends HibernateDaoSupport implements FilmDao {

	public List<Film> all() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void delete(Short id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Film get(Short id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void save(Film actor) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void update(Film actor) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Film> get(Actor actor) {
		Query query = getSession().createQuery("");
		return query.list();
	}

}

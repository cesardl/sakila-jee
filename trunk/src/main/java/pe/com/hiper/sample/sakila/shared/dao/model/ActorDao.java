package pe.com.hiper.sample.sakila.shared.dao.model;

import java.util.List;

import pe.com.hiper.sample.sakila.shared.dao.SakilaDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;

/**
 * 
 * @author pdiaz
 * 
 */
public interface ActorDao extends SakilaDao<Actor> {

	public List<Actor> getActors(String name, String lastName);
}

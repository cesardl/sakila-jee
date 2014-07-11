package pe.com.hiper.sample.sakila.shared.dao.model.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pe.com.hiper.sample.sakila.shared.dao.SakilaDaoSupport;
import pe.com.hiper.sample.sakila.shared.dao.model.ActorDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;

/**
 * 
 * @author pdiaz
 * 
 */
@Repository("actorDao")
public class ActorDaoImpl extends SakilaDaoSupport implements ActorDao {

	@SuppressWarnings("unchecked")
	public List<Actor> all() {
		Criteria criteria = getSession().createCriteria(Actor.class);
		return criteria.list();
	}

	public void delete(Short id) {
		getSession().delete(new Actor(id));
	}

	public Actor get(Short id) {
		return (Actor) getSession().get(Actor.class, id);
	}

	public Boolean save(Actor actor) {
		Serializable s = getSession().save(actor);
		if (s != null) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	public void update(Actor actor) {
		getSession().update(actor);
	}

	@SuppressWarnings("unchecked")
	public List<Actor> getActors(String name, String lastName) {
		Criteria criteria = getSession().createCriteria(Actor.class);
		if (name != null)
			criteria.add(Restrictions.like("firstName", name.concat("%")));
		if (lastName != null)
			criteria.add(Restrictions.like("lastName", lastName.concat("%")));

		return criteria.list();
	}
}

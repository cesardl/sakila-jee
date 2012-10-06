/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.shared.dao.model.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import pe.com.hiper.sakila.shared.dao.model.ActorDao;
import pe.com.hiper.sakila.shared.domain.mapping.Actor;

/**
 * 
 * @author pdiaz
 */
@Repository("actorDao")
public class ActorDaoImpl extends HibernateDaoSupport implements ActorDao {

	public List<Actor> all() {
		Criteria criteria = getSession().createCriteria(Actor.class);
		return criteria.list();
	}

	public void delete(Short id) {
		getHibernateTemplate().delete(new Actor(id));
	}

	public Actor get(Short id) {
		return getHibernateTemplate().get(Actor.class, id);
	}

	public void save(Actor actor) {
		getHibernateTemplate().save(actor);
	}

	public void update(Actor actor) {
		getHibernateTemplate().update(actor);
	}

	@Override
	public List<Actor> getActors(String name, String lastName) {
		Criteria criteria = getSession().createCriteria(Actor.class);
		if (name != null)
			criteria.add(Restrictions.like("firstName", name.concat("%")));
		if (lastName != null)
			criteria.add(Restrictions.like("lastName", lastName.concat("%")));

		return criteria.list();
	}
}

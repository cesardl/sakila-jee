/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.shared.dao.model;

import java.util.List;

import pe.com.hiper.sakila.shared.dao.Dao;
import pe.com.hiper.sakila.shared.domain.mapping.Actor;

/**
 * 
 * @author pdiaz
 */
public interface ActorDao extends Dao<Actor> {

	public List<Actor> getActors(String name, String lastName);
}

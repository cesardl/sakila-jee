/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.shared.dao.model;

import java.util.List;

import pe.com.hiper.sakila.shared.dao.Dao;
import pe.com.hiper.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sakila.shared.domain.mapping.Film;


/**
 *
 * @author pdiaz
 */
public interface FilmDao extends Dao<Film> {

    public List<Film> get(Actor actor);
}

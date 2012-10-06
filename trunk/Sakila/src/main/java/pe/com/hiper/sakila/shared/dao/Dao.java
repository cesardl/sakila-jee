/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.shared.dao;

import java.util.List;

/**
 *
 * @author pdiaz
 */
public interface Dao<T> {

    public T get(Short id);

    public List<T> all();

    public void save(T actor);

    public void update(T actor);

    public void delete(Short id);
}

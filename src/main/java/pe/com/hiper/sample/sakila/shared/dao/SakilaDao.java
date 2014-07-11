package pe.com.hiper.sample.sakila.shared.dao;

import java.util.List;

/**
 * 
 * @author pdiaz
 * 
 */
public interface SakilaDao<T> {

	public T get(Short id);

	public List<T> all();

	public Boolean save(T actor);

	public void update(T actor);

	public void delete(Short id);
}

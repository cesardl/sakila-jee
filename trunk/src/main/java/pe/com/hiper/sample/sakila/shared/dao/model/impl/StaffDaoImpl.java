package pe.com.hiper.sample.sakila.shared.dao.model.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import pe.com.hiper.sample.sakila.shared.dao.SakilaDaoSupport;
import pe.com.hiper.sample.sakila.shared.dao.model.StaffDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Staff;

/**
 * 
 * @author pdiaz
 * 
 */
@Repository("staffDao")
public class StaffDaoImpl extends SakilaDaoSupport implements StaffDao {

	@Override
	public Staff get(Short id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Staff> all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean save(Staff actor) {
		// TODO Auto-generated method stub
		return Boolean.TRUE;
	}

	@Override
	public void update(Staff actor) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(Short id) {
		// TODO Auto-generated method stub
	}

}

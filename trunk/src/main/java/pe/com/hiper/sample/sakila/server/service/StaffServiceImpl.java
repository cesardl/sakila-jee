package pe.com.hiper.sample.sakila.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import pe.com.hiper.sample.sakila.client.service.StaffService;
import pe.com.hiper.sample.sakila.shared.dao.model.StaffDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Staff;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author pdiaz
 * 
 */
@Controller
@Service("staffService")
public class StaffServiceImpl extends RemoteServiceServlet implements
		StaffService {

	private static final long serialVersionUID = 1L;

	@Autowired
	private StaffDao staffDao;

	public Staff get(String nombre, String password) {
		Staff staff = new Staff();
		staff.setEmail("atineo@hiper.com.pe");
		return staff;
	}
}

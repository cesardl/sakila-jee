package pe.com.hiper.sample.sakila.server.webservice;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface StaffWs {

	@WebResult(name = "login")
	public int login(@WebParam(name = "user") String user,
			@WebParam(name = "passwd") String passwd);

}

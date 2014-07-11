package pe.com.hiper.sample.sakila.server.webservice;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface ActorWs {

	@WebResult(name = "listByName")
	public String listByName(@WebParam(name = "name") String name,
			@WebParam(name = "lastName") String lastName);

}

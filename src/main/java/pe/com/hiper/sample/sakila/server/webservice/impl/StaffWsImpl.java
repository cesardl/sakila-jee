package pe.com.hiper.sample.sakila.server.webservice.impl;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import pe.com.hiper.sample.sakila.server.webservice.StaffWs;
import pe.com.hiper.sample.sakila.server.webservice.impl.build.SakilaWS;

@WebService(endpointInterface = "pe.com.hiper.sample.sakila.server.webservice.StaffWs")
public class StaffWsImpl implements StaffWs {

	@Autowired
	private SakilaWS sakilaWS;

	@Override
	public int login(@WebParam(name = "user") String user,
			@WebParam(name = "passwd") String passwd) {
		return sakilaWS.login(user, passwd);
	}

}

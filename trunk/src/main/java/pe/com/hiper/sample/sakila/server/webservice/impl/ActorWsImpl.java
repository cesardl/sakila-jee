package pe.com.hiper.sample.sakila.server.webservice.impl;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.hiper.sample.sakila.server.webservice.ActorWs;
import pe.com.hiper.sample.sakila.server.webservice.impl.build.SakilaWS;

@WebService(endpointInterface = "pe.com.hiper.sample.sakila.server.webservice.ActorWs")
public class ActorWsImpl implements ActorWs {

	private static final Logger logger = Logger.getLogger(ActorWsImpl.class);
	
	@Autowired
	private SakilaWS sakilaWS;

	@Override
	public String listByName(@WebParam(name = "name") String name,
			@WebParam(name = "lastName") String lastName) {
		logger.info("Get sakilaWS: " + this.sakilaWS);
		if (sakilaWS == null) {
			return "Operator on null";
		} else {
			return sakilaWS.actorListByName(name, lastName);
		}
	}

}

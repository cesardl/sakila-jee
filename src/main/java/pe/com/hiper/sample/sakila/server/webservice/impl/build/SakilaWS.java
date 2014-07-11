package pe.com.hiper.sample.sakila.server.webservice.impl.build;

import java.util.List;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pe.com.hiper.sample.sakila.shared.dao.model.ActorDao;
import pe.com.hiper.sample.sakila.shared.dao.model.StaffDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sample.sakila.shared.dto.ActorList;

import com.thoughtworks.xstream.XStream;

@Component
public class SakilaWS {

	@Autowired
	public ActorDao actorDao;

	@Autowired
	public StaffDao staffDao;

	@Produces("application/xml")
	public String actorListByName(String name, String lastName) {
		List<Actor> actors = actorDao.getActors(name, lastName);

		XStream xstream = new XStream();
		xstream.alias("actor", Actor.class);
		xstream.alias("actors", ActorList.class);
		xstream.addImplicitCollection(ActorList.class, "list");

		return xstream.toXML(new ActorList(actors));
	}

	public int login(String user, String passwd) {
		System.out.println(user + " - " + passwd);

		return 0;
	}

}

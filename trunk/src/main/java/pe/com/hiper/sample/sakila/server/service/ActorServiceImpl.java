package pe.com.hiper.sample.sakila.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import pe.com.hiper.sample.sakila.client.service.ActorService;
import pe.com.hiper.sample.sakila.shared.dao.model.ActorDao;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

/**
 * 
 * @author pdiaz
 * 
 */
@Controller
@Service("actorService")
public class ActorServiceImpl extends RemoteServiceServlet implements
		ActorService {

	private static final long serialVersionUID = 1L;

	@Autowired
	public ActorDao actorDao;

	public Actor findActor(Short id) {
		Actor actor = actorDao.get(id);
		return actor;
	}

	public PagingLoadResult<Actor> getActors(PagingLoadConfig config,
			String name, String lastName) {
		List<Actor> actors = actorDao.getActors(name, lastName);

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(
							actors,
							sort.getSortDir().comparator(
									new Comparator<Actor>() {

										public int compare(Actor a1, Actor a2) {
											if (sortField.equals("firstName")) {
												return a1
														.getFirstName()
														.compareTo(
																a2.getFirstName());
											} else if (sortField
													.equals("lastName")) {
												return a1
														.getLastName()
														.compareTo(
																a2.getLastName());
											} else if (sortField
													.equals("lastUpdate")) {
												return a1
														.getLastUpdate()
														.compareTo(
																a2.getLastUpdate());
											}
											return 0;
										}
									}));
				}
			}
		}

		ArrayList<Actor> sublist = new ArrayList<Actor>();
		int start = config.getOffset();
		int limit = actors.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(actors.get(i));
		}
		return new PagingLoadResultBean<Actor>(sublist, actors.size(),
				config.getOffset());
	}
}

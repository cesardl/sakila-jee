/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import pe.com.hiper.sakila.client.service.ActorService;
import pe.com.hiper.sakila.shared.dao.model.ActorDao;
import pe.com.hiper.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sakila.shared.dto.ActorDto;

/**
 * 
 * @author pdiaz
 */
@Controller
@Service("actorService")
public class ActorServiceImpl extends RemoteServiceServlet implements
		ActorService {

	private static final long serialVersionUID = -6439951931120697067L;

	@Autowired
	public ActorDao actorDao;

	public void setActorDao(ActorDao actorDao) {
		this.actorDao = actorDao;
	}

	public ActorDto findActor(Short id) {
		Actor actor = actorDao.get(id);
		ActorDto dto = new ActorDto(actor);
		return dto;
	}

	public PagingLoadResult<ActorDto> getActors(PagingLoadConfig config,
			String name, String lastName) {
		List<Actor> actors = actorDao.getActors(name, lastName);
		List<ActorDto> actorsDto = new ArrayList<ActorDto>(actors.size());
		for (Actor actor : actors) {
			actorsDto.add(new ActorDto(actor));
		}

		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(
							actorsDto,
							sort.getSortDir().comparator(
									new Comparator<ActorDto>() {

										public int compare(ActorDto a1,
												ActorDto a2) {
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

		ArrayList<ActorDto> sublist = new ArrayList<ActorDto>();
		int start = config.getOffset();
		int limit = actorsDto.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(actorsDto.get(i));
		}
		return new PagingLoadResultBean<ActorDto>(sublist, actorsDto.size(),
				config.getOffset());
	}
}

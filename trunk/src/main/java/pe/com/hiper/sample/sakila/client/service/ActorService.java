/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sample.sakila.client.service;

import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * 
 * @author pdiaz
 */
@RemoteServiceRelativePath("service/actorService")
public interface ActorService extends RemoteService {

	public Actor findActor(Short id);

	public PagingLoadResult<Actor> getActors(PagingLoadConfig config,
			String name, String lastName);
}

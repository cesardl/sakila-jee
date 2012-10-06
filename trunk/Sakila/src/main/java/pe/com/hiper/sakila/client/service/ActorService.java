/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import pe.com.hiper.sakila.shared.dto.ActorDto;

/**
 * 
 * @author pdiaz
 */
@RemoteServiceRelativePath("service/actorService")
public interface ActorService extends RemoteService {

	public ActorDto findActor(Short id);

	public PagingLoadResult<ActorDto> getActors(PagingLoadConfig config,
			String name, String lastName);
}

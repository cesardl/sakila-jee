/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import pe.com.hiper.sakila.shared.dto.ActorDto;

/**
 * 
 * @author pdiaz
 */
public interface ActorServiceAsync {

	public void findActor(Short id, AsyncCallback<ActorDto> callback);

	public void getActors(PagingLoadConfig config, String name,
			String lastName, AsyncCallback<PagingLoadResult<ActorDto>> callback);
}

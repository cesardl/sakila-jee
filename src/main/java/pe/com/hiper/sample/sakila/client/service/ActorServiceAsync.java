/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sample.sakila.client.service;

import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * 
 * @author pdiaz
 */
public interface ActorServiceAsync {

	public void findActor(Short id, AsyncCallback<Actor> callback);

	public void getActors(PagingLoadConfig config, String name,
			String lastName, AsyncCallback<PagingLoadResult<Actor>> callback);
}

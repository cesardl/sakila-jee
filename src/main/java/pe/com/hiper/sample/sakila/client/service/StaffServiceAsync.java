package pe.com.hiper.sample.sakila.client.service;

import pe.com.hiper.sample.sakila.shared.domain.mapping.Staff;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StaffServiceAsync {

	/**
	 * Metodo de login
	 * @param nombre
	 * @param password
	 * @param callback
	 */
	void get(String nombre, String password, AsyncCallback<Staff> callback);
}

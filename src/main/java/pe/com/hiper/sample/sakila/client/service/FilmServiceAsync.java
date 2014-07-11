package pe.com.hiper.sample.sakila.client.service;

import java.util.List;

import pe.com.hiper.sample.sakila.shared.dto.FilmDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FilmServiceAsync {

	void list(AsyncCallback<List<FilmDto>> callback);

}

package pe.com.hiper.sample.sakila.client.service;

import java.util.List;

import pe.com.hiper.sample.sakila.shared.dto.FilmDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/filmService")
public interface FilmService extends RemoteService {

	public List<FilmDto> list();

}

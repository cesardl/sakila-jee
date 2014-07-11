package pe.com.hiper.sample.sakila.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import pe.com.hiper.sample.sakila.client.service.FilmService;
import pe.com.hiper.sample.sakila.shared.dao.model.FilmDao;
import pe.com.hiper.sample.sakila.shared.dto.FilmDto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author pdiaz
 * 
 */
@Controller
@Service("filmService")
public class FilmServiceImpl extends RemoteServiceServlet implements
		FilmService {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FilmDao filmDao;

	public List<FilmDto> list() {
		return null;
	};
}

package org.sanmarcux.samples.sboot.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sboot.sakila.business.FilmBusiness;
import org.sanmarcux.samples.sboot.sakila.dao.FilmRepository;
import org.sanmarcux.samples.sboot.sakila.dao.LanguageRepository;
import org.sanmarcux.samples.sboot.sakila.dao.model.Film;
import org.sanmarcux.samples.sboot.sakila.dao.model.Language;
import org.sanmarcux.samples.sboot.sakila.dto.DTOFilm;
import org.sanmarcux.samples.sboot.sakila.exceptions.LanguageNotFoundException;
import org.sanmarcux.samples.sboot.sakila.exceptions.OperationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created on 29/04/2018.
 *
 * @author Cesardl
 */
@Service
public class FilmBusinessImpl implements FilmBusiness {

    private FilmRepository filmRepository;
    private LanguageRepository languageRepository;

    private ModelMapper modelMapper;

    @Autowired
    private FilmBusinessImpl(FilmRepository filmRepository, LanguageRepository languageRepository,
                             ModelMapper modelMapper) {
        this.filmRepository = filmRepository;
        this.languageRepository = languageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DTOFilm> list() {
        Iterable<Film> iterable = filmRepository.findAll();

        return StreamSupport.stream(iterable.spliterator(), false)
                .map(actor -> modelMapper.map(actor, DTOFilm.class))
                .collect(Collectors.toList());
    }

    @Override
    public DTOFilm create(final DTOFilm payload) {
        if (payload.getFilmId() != null) {
            throw new OperationNotAllowedException();
        }

        Film filmToSave = modelMapper.map(payload, Film.class);
        Language language = languageRepository.findById(Integer.valueOf(1).byteValue())
                .orElseThrow(LanguageNotFoundException::new);
        filmToSave.setLanguageByLanguageId(language);

        filmRepository.save(filmToSave);

        return modelMapper.map(filmToSave, DTOFilm.class);
    }

    @Override
    public List<DTOFilm> findFilmsByActor(final Short actorId) {
        List<Film> films = filmRepository.findAllByActor(actorId);

        return films.stream().map(film -> modelMapper.map(film, DTOFilm.class)).collect(Collectors.toList());
    }

}

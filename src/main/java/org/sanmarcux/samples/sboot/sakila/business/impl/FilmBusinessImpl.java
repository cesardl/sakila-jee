package org.sanmarcux.samples.sboot.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sboot.sakila.business.FilmBusiness;
import org.sanmarcux.samples.sboot.sakila.dao.FilmRepository;
import org.sanmarcux.samples.sboot.sakila.dao.LanguageRepository;
import org.sanmarcux.samples.sboot.sakila.dao.model.Film;
import org.sanmarcux.samples.sboot.sakila.dao.model.Language;
import org.sanmarcux.samples.sboot.sakila.dto.FilmDTO;
import org.sanmarcux.samples.sboot.sakila.exceptions.FilmNotFoundException;
import org.sanmarcux.samples.sboot.sakila.exceptions.LanguageNotFoundException;
import org.sanmarcux.samples.sboot.sakila.exceptions.OperationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public Page<FilmDTO> list(final Pageable pageable) {
        Page<Film> films = filmRepository.findAll(pageable);

        return new PageImpl<>(
                films.stream()
                        .map(film -> modelMapper.map(film, FilmDTO.class))
                        .collect(Collectors.toList()),
                films.getPageable(), films.getTotalElements());
    }

    @Override
    public FilmDTO create(final FilmDTO payload) {
        if (payload.getFilmId() != null) {
            throw new OperationNotAllowedException();
        }

        Film filmToSave = modelMapper.map(payload, Film.class);
        Language language = languageRepository.findById(payload.getLanguage().getId().byteValue())
                .orElseThrow(LanguageNotFoundException::new);
        filmToSave.setLanguageByLanguageId(language);

        filmRepository.save(filmToSave);

        return modelMapper.map(filmToSave, FilmDTO.class);
    }

    @Override
    public List<FilmDTO> findFilmsByActor(final Short actorId) {
        List<Film> films = filmRepository.findAllByActor(actorId);

        return films.stream().map(film -> modelMapper.map(film, FilmDTO.class)).collect(Collectors.toList());
    }

    @Override
    public FilmDTO get(final Short filmId) {
        return filmRepository.findById(filmId)
                .map(film -> modelMapper.map(film, FilmDTO.class))
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }
}

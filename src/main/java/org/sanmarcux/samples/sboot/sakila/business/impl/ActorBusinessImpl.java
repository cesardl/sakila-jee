package org.sanmarcux.samples.sboot.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sboot.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sboot.sakila.dao.ActorRepository;
import org.sanmarcux.samples.sboot.sakila.dao.FilmActorRepository;
import org.sanmarcux.samples.sboot.sakila.dao.FilmRepository;
import org.sanmarcux.samples.sboot.sakila.dao.LanguageRepository;
import org.sanmarcux.samples.sboot.sakila.dao.model.*;
import org.sanmarcux.samples.sboot.sakila.dto.DTOActor;
import org.sanmarcux.samples.sboot.sakila.dto.DTOFilm;
import org.sanmarcux.samples.sboot.sakila.exceptions.ActorNotFoundException;
import org.sanmarcux.samples.sboot.sakila.exceptions.LanguageNotFoundException;
import org.sanmarcux.samples.sboot.sakila.exceptions.OperationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@Service
public class ActorBusinessImpl implements ActorBusiness {

    private ActorRepository actorRepository;
    private FilmRepository filmRepository;
    private FilmActorRepository filmActorRepository;
    private LanguageRepository languageRepository;

    private ModelMapper modelMapper;

    @Autowired
    private ActorBusinessImpl(ActorRepository actorRepository, FilmRepository filmRepository,
                              FilmActorRepository filmActorRepository,
                              LanguageRepository languageRepository, ModelMapper modelMapper) {
        this.actorRepository = actorRepository;
        this.filmRepository = filmRepository;
        this.filmActorRepository = filmActorRepository;
        this.languageRepository = languageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DTOActor> list() {
        Iterable<Actor> iterable = actorRepository.findAll();

        return StreamSupport.stream(iterable.spliterator(), false)
                .map(actor -> modelMapper.map(actor, DTOActor.class))
                .collect(Collectors.toList());
    }

    @Override
    public DTOActor create(final DTOActor payload) {
        if (payload.getActorId() != null) {
            throw new OperationNotAllowedException();
        }

        return modelMapper.map(
                actorRepository.save(
                        modelMapper.map(payload, Actor.class)), DTOActor.class);
    }

    @Override
    public DTOActor modify(final Short actorId, final DTOActor payload) {
        Optional<Actor> actorOptional = actorRepository.findById(actorId);

        Actor actor = actorOptional.map(
                a -> {
                    modelMapper.map(payload, a);
                    return a;
                })
                .orElseThrow(() -> new ActorNotFoundException(actorId));

        actorRepository.save(actor);

        return modelMapper.map(actor, DTOActor.class);
    }

    @Override
    public DTOActor get(final Short actorId) {
        return actorRepository.findById(actorId)
                .map(actor -> modelMapper.map(actor, DTOActor.class))
                .orElseThrow(() -> new ActorNotFoundException(actorId));
    }

    @Override
    public void delete(final Short actorId) {
        actorRepository.deleteById(actorId);
    }

    @Override
    public List<DTOFilm> listFilms(final Short actorId) {
        List<Film> films = filmRepository.findAllByActor(actorId);

        return films.stream().map(film -> modelMapper.map(film, DTOFilm.class)).collect(Collectors.toList());
    }

    @Override
    public DTOFilm createFilm(final Short actorId, final DTOFilm payload) {
        if (payload.getFilmId() != null) {
            throw new OperationNotAllowedException();
        }

        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNotFoundException(actorId));

        Film filmToSave = modelMapper.map(payload, Film.class);
        Language language = languageRepository.findById(Integer.valueOf(1).byteValue())
                .orElseThrow(LanguageNotFoundException::new);
        filmToSave.setLanguageByLanguageId(language);

        Film savedFilm = filmRepository.save(filmToSave);

        FilmActor filmActor = new FilmActor();
        filmActor.setId(new FilmActorId(actor.getActorId(), savedFilm.getFilmId()));
        filmActorRepository.save(filmActor);

        return modelMapper.map(savedFilm, DTOFilm.class);
    }

    @Override
    public DTOFilm getFilm(final Short actorId, final Short filmId) {
        return filmActorRepository.findById(new FilmActorId(actorId, filmId))
                .map(r -> modelMapper.map(r.getFilm(), DTOFilm.class))
                .orElseThrow(() -> new OperationNotAllowedException("The actor doesn't participate in film"));
    }

    @Override
    public void deleteFilm(final Short actorId, final Short filmId) {
        filmActorRepository.deleteById(new FilmActorId(actorId, filmId));
    }
}

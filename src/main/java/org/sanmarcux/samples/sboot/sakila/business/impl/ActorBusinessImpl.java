package org.sanmarcux.samples.sboot.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sboot.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sboot.sakila.dao.ActorRepository;
import org.sanmarcux.samples.sboot.sakila.dao.FilmActorRepository;
import org.sanmarcux.samples.sboot.sakila.dao.model.Actor;
import org.sanmarcux.samples.sboot.sakila.dao.model.FilmActor;
import org.sanmarcux.samples.sboot.sakila.dao.model.FilmActorId;
import org.sanmarcux.samples.sboot.sakila.dto.DTOActor;
import org.sanmarcux.samples.sboot.sakila.dto.DTOFilm;
import org.sanmarcux.samples.sboot.sakila.exceptions.ActorNotFoundException;
import org.sanmarcux.samples.sboot.sakila.exceptions.OperationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private FilmActorRepository filmActorRepository;

    private ModelMapper modelMapper;

    @Autowired
    private ActorBusinessImpl(ActorRepository actorRepository, FilmActorRepository filmActorRepository,
                              ModelMapper modelMapper) {
        this.actorRepository = actorRepository;
        this.filmActorRepository = filmActorRepository;
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
        Actor actor = modelMapper.map(payload, Actor.class);
        actor.setActorId(actorId);

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
    public void createFilmParticipation(final Short actorId, final Short filmId) {
        FilmActor filmActor = new FilmActor();
        filmActor.setId(new FilmActorId(actorId, filmId));
        filmActorRepository.save(filmActor);
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

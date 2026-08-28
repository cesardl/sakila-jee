package org.sanmarcux.samples.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sakila.dao.ActorRepository;
import org.sanmarcux.samples.sakila.dao.FilmActorRepository;
import org.sanmarcux.samples.sakila.dao.model.Actor;
import org.sanmarcux.samples.sakila.dao.model.FilmActor;
import org.sanmarcux.samples.sakila.dao.model.FilmActorId;
import org.sanmarcux.samples.sakila.dto.ActorDTO;
import org.sanmarcux.samples.sakila.dto.FilmDTO;
import org.sanmarcux.samples.sakila.exceptions.ActorNotFoundException;
import org.sanmarcux.samples.sakila.exceptions.OperationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@Service
public class ActorBusinessImpl implements ActorBusiness {

    private final ActorRepository actorRepository;
    private final FilmActorRepository filmActorRepository;

    private final ModelMapper modelMapper;

    // public, not private: @Transactional on modify() makes Spring CGLIB-proxy this class,
    // and CGLIB cannot subclass a type whose only constructor is private.
    @Autowired
    public ActorBusinessImpl(ActorRepository actorRepository,
                              FilmActorRepository filmActorRepository,
                              ModelMapper modelMapper) {
        this.actorRepository = actorRepository;
        this.filmActorRepository = filmActorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ActorDTO> list(final Pageable pageable) {
        Page<Actor> actors = actorRepository.findAll(pageable);

        return new PageImpl<>(
                actors.stream()
                        .map(actor -> modelMapper.map(actor, ActorDTO.class))
                        .collect(Collectors.toList()),
                actors.getPageable(), actors.getTotalElements());
    }

    @Override
    public ActorDTO create(final ActorDTO payload) {
        if (payload.getActorId() != null) {
            throw new OperationNotAllowedException();
        }

        return modelMapper.map(
                actorRepository.save(
                        modelMapper.map(payload, Actor.class)), ActorDTO.class);
    }

    @Transactional
    @Override
    public ActorDTO modify(final Integer actorId, final ActorDTO payload) {
        // PATCH is a partial update, so merge onto the managed row instead of mapping the
        // payload onto a fresh Actor: that nulled every field the caller left out, which
        // wiped first_name/last_name and blew up on the NOT NULL last_update.
        // ModelMapper is configured with skipNullEnabled, so absent fields are left alone.
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNotFoundException(actorId));

        modelMapper.map(payload, actor);
        actor.setActorId(actorId);
        actor.setLastUpdate(new Date());

        return modelMapper.map(actorRepository.save(actor), ActorDTO.class);
    }

    @Override
    public ActorDTO get(final Integer actorId) {
        return actorRepository.findById(actorId)
                .map(actor -> modelMapper.map(actor, ActorDTO.class))
                .orElseThrow(() -> new ActorNotFoundException(actorId));
    }

    @Override
    public void delete(final Integer actorId) {
        actorRepository.deleteById(actorId);
    }

    @Override
    public void createFilmParticipation(final Integer actorId, final Integer filmId) {
        FilmActor filmActor = new FilmActor();
        filmActor.setId(new FilmActorId(actorId, filmId));
        filmActor.setLastUpdate(LocalDateTime.now());
        filmActorRepository.save(filmActor);
    }

    @Override
    public FilmDTO getFilm(final Integer actorId, final Integer filmId) {
        return filmActorRepository.findByIdFetchingFilm(new FilmActorId(actorId, filmId))
                .map(filmActor -> modelMapper.map(filmActor.getFilm(), FilmDTO.class))
                .orElseThrow(() -> new OperationNotAllowedException("The actor doesn't participate in film"));
    }

    @Override
    public void deleteFilm(final Integer actorId, final Integer filmId) {
        filmActorRepository.deleteById(new FilmActorId(actorId, filmId));
    }
}

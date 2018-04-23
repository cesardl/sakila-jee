package org.sanmarcux.samples.sboot.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sboot.sakila.business.ActorBusiness;
import org.sanmarcux.samples.sboot.sakila.dao.ActorRepository;
import org.sanmarcux.samples.sboot.sakila.dao.model.Actor;
import org.sanmarcux.samples.sboot.sakila.entities.DTOIntActor;
import org.sanmarcux.samples.sboot.sakila.exceptions.ActorNotFoundException;
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

    private ModelMapper modelMapper;

    @Autowired
    private ActorBusinessImpl(ActorRepository actorRepository, ModelMapper modelMapper) {
        this.actorRepository = actorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DTOIntActor> list() {
        Iterable<Actor> iterable = actorRepository.findAll();

        return StreamSupport.stream(iterable.spliterator(), false)
                .map(actor -> modelMapper.map(actor, DTOIntActor.class))
                .collect(Collectors.toList());
    }

    @Override
    public DTOIntActor create(final DTOIntActor payload) {
        if (payload.getActorId() != null) {
            throw new OperationNotAllowedException();
        }

        return modelMapper.map(
                actorRepository.save(
                        modelMapper.map(payload, Actor.class)), DTOIntActor.class);
    }

    @Override
    public DTOIntActor modify(final Short actorId, final DTOIntActor payload) {
        Optional<Actor> actorOptional = actorRepository.findById(actorId);

        Actor actor = actorOptional.map(
                a -> {
                    modelMapper.map(payload, a);
                    return a;
                })
                .orElseThrow(() -> new ActorNotFoundException(actorId));

        actorRepository.save(actor);

        return modelMapper.map(actor, DTOIntActor.class);
    }

    @Override
    public DTOIntActor get(final Short actorId) {
        return actorRepository.findById(actorId)
                .map(actor -> modelMapper.map(actor, DTOIntActor.class))
                .orElseThrow(() -> new ActorNotFoundException(actorId));
    }

    @Override
    public void delete(final Short actorId) {
        actorRepository.deleteById(actorId);
    }
}

package org.sanmarcux.samples.sboot.sakila.dao;

import org.sanmarcux.samples.sboot.sakila.dao.model.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@Transactional
public interface ActorRepository extends CrudRepository<Actor, Short> {

    /**
     * This method will find an DTOIntActor instance in the database by its last name.
     * Note that this method is not implemented and its working code will be
     * automatically generated from its signature by Spring Data JPA.
     */
    Actor findByLastName(String lastName);
}

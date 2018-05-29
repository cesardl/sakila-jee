package org.sanmarcux.samples.sakila.dao;

import org.sanmarcux.samples.sakila.dao.model.Staff;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created on 28/04/2018.
 *
 * @author Cesardl
 */
public interface StaffRepository extends CrudRepository<Staff, Byte> {

    Optional<Staff> findByUsername(String username);
}

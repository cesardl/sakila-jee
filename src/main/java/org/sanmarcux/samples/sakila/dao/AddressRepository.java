package org.sanmarcux.samples.sakila.dao;

import org.sanmarcux.samples.sakila.dao.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Integer> {
}

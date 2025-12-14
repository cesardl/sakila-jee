package org.sanmarcux.samples.sakila.dao;

import org.sanmarcux.samples.sakila.dao.model.Store;
import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<Store, Integer> {
}

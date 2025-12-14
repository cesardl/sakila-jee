package org.sanmarcux.samples.sakila.dao;

import org.sanmarcux.samples.sakila.dao.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 28/04/2018.
 *
 * @author Cesardl
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}

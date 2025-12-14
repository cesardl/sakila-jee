package org.sanmarcux.samples.sakila.business;

import org.sanmarcux.samples.sakila.dto.CustomerDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

/**
 * Created on 14/12/2025.
 *
 * @author Cesardl
 */
public interface CustomerBusiness {

    CollectionModel<EntityModel<CustomerDTO>> findAll();

    EntityModel<CustomerDTO> save(CustomerDTO payload);

    EntityModel<CustomerDTO> get(Integer customerId);

    EntityModel<CustomerDTO> modify(Integer id, CustomerDTO payload);

    void deleteCustomer(Integer id);
}

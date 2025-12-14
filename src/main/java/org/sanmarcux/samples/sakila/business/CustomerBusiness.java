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

    CustomerDTO save(CustomerDTO payload);

    EntityModel<CustomerDTO> get(Short customerId);

    CustomerDTO modify(Short id, CustomerDTO payload);

    void deleteCustomer(Short id);
}

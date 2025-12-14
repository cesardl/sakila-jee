package org.sanmarcux.samples.sakila.controller;

import org.sanmarcux.samples.sakila.business.CustomerBusiness;
import org.sanmarcux.samples.sakila.dto.CustomerDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 14/12/2025.
 *
 * @author Cesardl
 */
@RestController
public class CustomerRestController {

    private final CustomerBusiness business;

    public CustomerRestController(CustomerBusiness business) {
        this.business = business;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/customers")
    public CollectionModel<EntityModel<CustomerDTO>> all() {
        return business.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/customers")
    public CustomerDTO newCustomer(@RequestBody CustomerDTO newCustomer) {
        return business.save(newCustomer);
    }

    // Single item
    @GetMapping("/customers/{id}")
    public EntityModel<CustomerDTO> one(@PathVariable Short id) {
        return business.get(id);
    }

    @PutMapping("/customers/{id}")
    public CustomerDTO replaceCustomer(@RequestBody CustomerDTO newCustomer, @PathVariable Short id) {
        return business.modify(id, newCustomer);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Short id) {
        business.deleteCustomer(id);
    }
}

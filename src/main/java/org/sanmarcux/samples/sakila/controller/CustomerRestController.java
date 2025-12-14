package org.sanmarcux.samples.sakila.controller;

import org.sanmarcux.samples.sakila.business.CustomerBusiness;
import org.sanmarcux.samples.sakila.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 14/12/2025.
 *
 * @author Cesardl
 */
@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRestController.class);

    private final CustomerBusiness business;

    public CustomerRestController(CustomerBusiness business) {
        this.business = business;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping
    public CollectionModel<EntityModel<CustomerDTO>> all() {
        LOG.info("Invoking Rest Service listCustomers");
        return business.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping()
    public ResponseEntity<?> newCustomer(@RequestBody CustomerDTO newCustomer) {
        LOG.info("Invoking Rest Service createCustomer");

        EntityModel<CustomerDTO> entityModel = business.save(newCustomer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    // Single item
    @GetMapping("/{id}")
    public EntityModel<CustomerDTO> one(@PathVariable Integer id) {
        LOG.info("Invoking Rest Service getCustomer");
        return business.get(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceCustomer(@RequestBody CustomerDTO newCustomer, @PathVariable Integer id) {
        LOG.info("Invoking Rest Service modifyCustomer");

        EntityModel<CustomerDTO> entityModel = business.modify(id, newCustomer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        LOG.info("Invoking Rest Service deleteCustomer");
        business.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}

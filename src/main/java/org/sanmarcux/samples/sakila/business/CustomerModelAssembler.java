package org.sanmarcux.samples.sakila.business;

import org.sanmarcux.samples.sakila.controller.CustomerRestController;
import org.sanmarcux.samples.sakila.dto.CustomerDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<CustomerDTO, EntityModel<CustomerDTO>> {

    @Override
    public EntityModel<CustomerDTO> toModel(CustomerDTO customer) {

        return EntityModel.of(customer, //
                linkTo(methodOn(CustomerRestController.class).one(customer.getCustomerId())).withSelfRel(),
                linkTo(methodOn(CustomerRestController.class).all()).withRel("customers"));
    }
}

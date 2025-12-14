package org.sanmarcux.samples.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sakila.business.CustomerBusiness;
import org.sanmarcux.samples.sakila.controller.CustomerRestController;
import org.sanmarcux.samples.sakila.dao.CustomerRepository;
import org.sanmarcux.samples.sakila.dao.model.Customer;
import org.sanmarcux.samples.sakila.dto.CustomerDTO;
import org.sanmarcux.samples.sakila.exceptions.CustomerNotFoundException;
import org.sanmarcux.samples.sakila.exceptions.OperationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created on 14/12/2025.
 *
 * @author Cesardl
 */
@Service
public class CustomerBusinessImpl implements CustomerBusiness {

    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    @Autowired
    private CustomerBusinessImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CollectionModel<EntityModel<CustomerDTO>> findAll() {
        List<EntityModel<CustomerDTO>> customers = customerRepository.findAll().stream()
                .map(customer -> {
                    CustomerDTO dto = modelMapper.map(customer, CustomerDTO.class);
                    return EntityModel.of(dto,
                            linkTo(methodOn(CustomerRestController.class).one(customer.getCustomerId())).withSelfRel(),
                            linkTo(methodOn(CustomerRestController.class).all()).withRel("customers"));
                })
                .collect(Collectors.toList());

        return CollectionModel.of(customers, linkTo(methodOn(CustomerRestController.class).all()).withSelfRel());
    }

    @Override
    public CustomerDTO save(CustomerDTO payload) {
        if (payload.getCustomerId() != null) {
            throw new OperationNotAllowedException();
        }

        return modelMapper.map(
                customerRepository.save(
                        modelMapper.map(payload, Customer.class)), CustomerDTO.class);
    }

    public EntityModel<CustomerDTO> get(final Short customerId) {
        CustomerDTO dto = customerRepository.findById(customerId)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return EntityModel.of(dto, //
                linkTo(methodOn(CustomerRestController.class).one(customerId)).withSelfRel(),
                linkTo(methodOn(CustomerRestController.class).all()).withRel("customers"));
    }

    @Override
    public CustomerDTO modify(final Short id, final CustomerDTO payload) {
        Customer customer = modelMapper.map(payload, Customer.class);

        Customer r = customerRepository.findById(id)
                .map(c -> {
                    c.setFirstName(payload.getFirstName());
                    c.setLastName(payload.getLastName());
                    return customerRepository.save(customer);
                })
                .orElseGet(() -> customerRepository.save(customer));

        return modelMapper.map(r, CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(final Short id) {
        customerRepository.deleteById(id);
    }
}

package org.sanmarcux.samples.sakila.business.impl;

import org.modelmapper.ModelMapper;
import org.sanmarcux.samples.sakila.business.CustomerBusiness;
import org.sanmarcux.samples.sakila.business.CustomerModelAssembler;
import org.sanmarcux.samples.sakila.controller.CustomerRestController;
import org.sanmarcux.samples.sakila.dao.AddressRepository;
import org.sanmarcux.samples.sakila.dao.CustomerRepository;
import org.sanmarcux.samples.sakila.dao.StoreRepository;
import org.sanmarcux.samples.sakila.dao.model.Address;
import org.sanmarcux.samples.sakila.dao.model.Customer;
import org.sanmarcux.samples.sakila.dao.model.Store;
import org.sanmarcux.samples.sakila.dto.CustomerDTO;
import org.sanmarcux.samples.sakila.exceptions.CustomerNotFoundException;
import org.sanmarcux.samples.sakila.exceptions.OperationNotAllowedException;
import org.sanmarcux.samples.sakila.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final AddressRepository addressRepository;
    private final StoreRepository storeRepository;
    private final CustomerModelAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerBusinessImpl(CustomerRepository customerRepository, AddressRepository addressRepository, StoreRepository storeRepository, CustomerModelAssembler assembler, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.storeRepository = storeRepository;
        this.assembler = assembler;
        this.modelMapper = modelMapper;
    }

    @Override
    public CollectionModel<EntityModel<CustomerDTO>> findAll() {
        List<EntityModel<CustomerDTO>> customers = customerRepository.findAll().stream().map(customer -> {
            CustomerDTO dto = modelMapper.map(customer, CustomerDTO.class);
            return assembler.toModel(dto);
        }).collect(Collectors.toList());

        return CollectionModel.of(customers, linkTo(methodOn(CustomerRestController.class).all()).withSelfRel());
    }

    @Override
    public EntityModel<CustomerDTO> save(CustomerDTO payload) {
        if (payload.getCustomerId() != null) {
            throw new OperationNotAllowedException();
        }

        Customer c = modelMapper.map(payload, Customer.class);

        // Both are @NotNull on CustomerDTO, so @Valid has already rejected a payload that
        // omits them. Resolve rather than trust: a client-supplied id that does not exist
        // would otherwise fail late, as a foreign key violation.
        c.setStore(resolveStore(payload.getStore().getStoreId()));
        c.setAddress(resolveAddress(payload.getAddress().getAddressId()));

        // customer.active is NOT NULL DEFAULT TRUE, but the entity field is a primitive, so
        // an omitted flag reached the insert as an explicit 0 and every customer created
        // through the API was born inactive. Absent now means the column default.
        c.setActive(payload.getActive() == null || payload.getActive());

        // create_date is the one timestamp the schema does NOT default; last_update is
        // DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP and is mapped read-only.
        c.setCreateDate(LocalDateTime.now());

        CustomerDTO dto = modelMapper.map(customerRepository.save(c), CustomerDTO.class);
        return assembler.toModel(dto);
    }

    public EntityModel<CustomerDTO> get(final Integer customerId) {
        CustomerDTO dto = customerRepository.findById(customerId).map(customer -> modelMapper.map(customer, CustomerDTO.class)).orElseThrow(() -> new CustomerNotFoundException(customerId));

        return assembler.toModel(dto);
    }

    @Transactional
    @Override
    public EntityModel<CustomerDTO> modify(final Integer id, final CustomerDTO payload) {
        Customer managedCustomer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        if (payload.getAddress().getAddressId() != null) {
            managedCustomer.setAddress(resolveAddress(payload.getAddress().getAddressId()));
        }

        if (payload.getStore().getStoreId() != null) {
            managedCustomer.setStore(resolveStore(payload.getStore().getStoreId()));
        }

        modelMapper.map(payload, managedCustomer);

        CustomerDTO dto = modelMapper.map(customerRepository.save(managedCustomer), CustomerDTO.class);
        return assembler.toModel(dto);
    }

    @Override
    public void deleteCustomer(final Integer id) {
        customerRepository.deleteById(id);
    }

    private Store resolveStore(final Integer storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
    }

    private Address resolveAddress(final Integer addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    }
}

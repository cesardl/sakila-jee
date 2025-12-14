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

import java.util.Date;
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

        Store s = new Store();
        s.setStoreId(2);
        c.setStore(s);

        Address a = new Address();
        a.setAddressId(591);
        c.setAddress(a);

        Date d = new Date();
        c.setCreateDate(d);
        c.setLastUpdate(d);

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
            Address newAddress = addressRepository.findById(payload.getAddress().getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
            managedCustomer.setAddress(newAddress);
        }

        if (payload.getStore().getStoreId() != null) {
            Store newStore = storeRepository.findById(payload.getStore().getStoreId()).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
            managedCustomer.setStore(newStore);
        }

        modelMapper.map(payload, managedCustomer);

        managedCustomer.setLastUpdate(new Date());

        CustomerDTO dto = modelMapper.map(customerRepository.save(managedCustomer), CustomerDTO.class);
        return assembler.toModel(dto);
    }

    @Override
    public void deleteCustomer(final Integer id) {
        customerRepository.deleteById(id);
    }
}

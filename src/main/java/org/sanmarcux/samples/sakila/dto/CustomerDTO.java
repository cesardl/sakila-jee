package org.sanmarcux.samples.sakila.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Created on 14/12/2025.
 *
 * @author Cesardl
 */
public class CustomerDTO {

    private Integer customerId;

    @Valid
    @NotNull
    private StoreDTO store;

    private String firstName;

    private String lastName;

    private String email;

    @Valid
    @NotNull
    private AddressDTO address;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}

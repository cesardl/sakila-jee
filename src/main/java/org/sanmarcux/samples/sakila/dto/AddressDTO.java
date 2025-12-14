package org.sanmarcux.samples.sakila.dto;

import jakarta.validation.constraints.NotNull;

public class AddressDTO {

    @NotNull
    private Integer addressId;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}

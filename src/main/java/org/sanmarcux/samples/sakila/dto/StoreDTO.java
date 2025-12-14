package org.sanmarcux.samples.sakila.dto;

import javax.validation.constraints.NotNull;

public class StoreDTO {

    @NotNull
    private Integer storeId;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}

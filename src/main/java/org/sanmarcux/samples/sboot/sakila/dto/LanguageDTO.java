package org.sanmarcux.samples.sboot.sakila.dto;

import javax.validation.constraints.NotNull;

/**
 * Created on 01/05/2018.
 *
 * @author Cesardl
 */
public class LanguageDTO {

    @NotNull
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

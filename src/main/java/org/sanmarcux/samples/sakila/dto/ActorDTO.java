package org.sanmarcux.samples.sakila.dto;

import javax.validation.constraints.NotNull;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
public class ActorDTO {

    private Integer actorId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
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
}

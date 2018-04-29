package org.sanmarcux.samples.sboot.sakila.dto;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
public class DTOActor {

    private Short actorId;
    private String firstName;
    private String lastName;

    public Short getActorId() {
        return actorId;
    }

    public void setActorId(Short actorId) {
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

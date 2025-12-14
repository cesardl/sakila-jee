package org.sanmarcux.samples.sakila.dto;

/**
 * Created on 14/12/2025.
 *
 * @author Cesardl
 */
public class CustomerDTO {

    private Short customerId;
    private String firstName;
    private String lastName;
    private String email;

    public Short getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Short customerId) {
        this.customerId = customerId;
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
}

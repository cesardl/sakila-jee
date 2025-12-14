package org.sanmarcux.samples.sakila.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Integer id) {
        super("Could not find customer " + id);
    }
}

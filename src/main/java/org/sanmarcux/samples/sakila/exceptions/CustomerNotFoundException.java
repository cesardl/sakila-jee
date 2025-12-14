package org.sanmarcux.samples.sakila.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Short id) {
        super("Could not find customer " + id);
    }
}

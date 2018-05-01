package org.sanmarcux.samples.sboot.sakila.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FilmNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8732907115274367029L;

    public FilmNotFoundException(final Short filmId) {
        super("Could not find film with id '" + filmId + "'.");
    }
}

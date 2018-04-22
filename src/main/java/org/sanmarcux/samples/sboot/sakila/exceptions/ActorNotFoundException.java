package org.sanmarcux.samples.sboot.sakila.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActorNotFoundException extends RuntimeException {

    public ActorNotFoundException(final Short actorId) {
        super("Could not find actor '" + actorId + "'.");
    }
}

package org.sanmarcux.samples.sakila.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 21/04/2018.
 *
 * @author Cesardl
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActorNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8732907115274367029L;

    public ActorNotFoundException(final Short actorId) {
        super("Could not find actor with id '" + actorId + "'.");
    }
}

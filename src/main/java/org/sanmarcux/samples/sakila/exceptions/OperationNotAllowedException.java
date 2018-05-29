package org.sanmarcux.samples.sakila.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 08/12/2017.
 *
 * @author Cesardl
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperationNotAllowedException extends RuntimeException {

    private static final long serialVersionUID = 3817729211331814408L;

    public OperationNotAllowedException() {
        super();
    }

    public OperationNotAllowedException(String message) {
        super(message);
    }
}

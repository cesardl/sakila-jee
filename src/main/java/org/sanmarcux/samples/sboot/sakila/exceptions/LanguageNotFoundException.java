package org.sanmarcux.samples.sboot.sakila.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 28/04/2018.
 *
 * @author Cesardl
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class LanguageNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5758316984509429266L;
}

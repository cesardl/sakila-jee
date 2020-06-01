package org.sanmarcux.samples.sakila.dao.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

/**
 * Created for sakila-jee on 31/05/2020.
 *
 * @author Cesardl
 */
@Converter
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        if (rating == null) {
            return null;
        }
        return rating.getCode();
    }

    @Override
    public Rating convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Rating.values())
                .filter(r -> r.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

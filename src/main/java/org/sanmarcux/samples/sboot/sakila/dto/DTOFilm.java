package org.sanmarcux.samples.sboot.sakila.dto;

/**
 * Created on 22/04/2018.
 *
 * @author Cesardl
 */
public class DTOFilm {

    private Short filmId;
    private String title;
    private String description;

    public Short getFilmId() {
        return filmId;
    }

    public void setFilmId(Short filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

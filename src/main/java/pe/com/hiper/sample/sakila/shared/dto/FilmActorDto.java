/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sample.sakila.shared.dto;

import java.util.Date;

import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Film;
import pe.com.hiper.sample.sakila.shared.domain.mapping.FilmActor;
import pe.com.hiper.sample.sakila.shared.domain.mapping.FilmActorId;

/**
 *
 * @author pdiaz
 */
public class FilmActorDto {

    private FilmActorId id;
    private Actor actor;
    private Film film;
    private Date lastUpdate;

    public FilmActorDto() {
    }

    public FilmActorDto(FilmActorId id, Actor actor, Film film, Date lastUpdate) {
        this.id = id;
        this.actor = actor;
        this.film = film;
        this.lastUpdate = lastUpdate;
    }

    public FilmActorDto(FilmActor filmActor) {
        this.id = filmActor.getId();
        this.actor = filmActor.getActor();
        this.film = filmActor.getFilm();
        this.lastUpdate = filmActor.getLastUpdate();
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public FilmActorId getId() {
        return id;
    }

    public void setId(FilmActorId id) {
        this.id = id;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

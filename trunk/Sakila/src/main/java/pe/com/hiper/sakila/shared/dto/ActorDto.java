/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.shared.dto;

import java.util.Date;

import pe.com.hiper.sakila.shared.domain.mapping.Actor;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author pdiaz
 */
public class ActorDto implements IsSerializable {

	private Short actorId;
	private String firstName;
	private String lastName;
	private Date lastUpdate;

	public ActorDto() {
	}

	public ActorDto(Short actorId, String firstName, String lastName,
			Date lastUpdate) {
		this.actorId = actorId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.lastUpdate = lastUpdate;
	}

	public ActorDto(Actor actor) {
		this.actorId = actor.getActorId();
		this.firstName = actor.getFirstName();
		this.lastName = actor.getLastName();
		this.lastUpdate = actor.getLastUpdate();
	}

	public Short getActorId() {
		return actorId;
	}

	public void setActorId(Short actorId) {
		this.actorId = actorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "Actor{" + "id=" + actorId + ", name='" + firstName + "'"
				+ ", lastName=" + lastName + ", lastUpdate=" + lastUpdate + "}";
	}
}

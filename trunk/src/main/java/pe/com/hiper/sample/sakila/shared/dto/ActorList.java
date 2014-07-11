package pe.com.hiper.sample.sakila.shared.dto;

import java.util.ArrayList;
import java.util.List;

import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;

public class ActorList {
	private List<Actor> list;

	public ActorList() {
		list = new ArrayList<Actor>();
	}

	public ActorList(List<Actor> list) {
		this.list = list;
	}

	public void add(Actor p) {
		list.add(p);
	}
}

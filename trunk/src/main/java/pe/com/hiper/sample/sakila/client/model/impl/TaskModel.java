package pe.com.hiper.sample.sakila.client.model.impl;

import pe.com.hiper.sample.sakila.client.model.NamedModel;

public class TaskModel extends NamedModel {

	private int type;

	public TaskModel(String name, int type) {
		super(name);
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

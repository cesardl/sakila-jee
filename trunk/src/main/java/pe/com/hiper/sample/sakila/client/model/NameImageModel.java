/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package pe.com.hiper.sample.sakila.client.model;

import com.sencha.gxt.data.shared.ModelKeyProvider;

public class NameImageModel {

	public static ModelKeyProvider<NameImageModel> KP = new ModelKeyProvider<NameImageModel>() {
		@Override
		public String getKey(NameImageModel item) {
			return item.getName();
		}
	};

	private String name;
	private String image;

	public String getImage() {
		return image;
	}

	public NameImageModel(String name, String image) {
		this.name = name;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

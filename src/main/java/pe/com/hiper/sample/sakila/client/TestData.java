/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package pe.com.hiper.sample.sakila.client;

import java.util.ArrayList;
import java.util.List;

import pe.com.hiper.sample.sakila.client.model.Data;

public class TestData {
	public static final int TYPE_ACTOR = 1;
	public static final int TYPE_FILM = 2;
	public static final int TYPE_CHART_COLUMN = 3;

	public static String DUMMY_TEXT_SHORT = "Lorem Ipsum is simply dummy text of the "
			+ "printing and typesetting industry. Lorem Ipsum has been the industry's standard "
			+ "dummy text ever since the 1500s.";

	private static final String[] monthsFull = new String[] { "January",
			"February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	public static List<Data> getData(int size, double min, double scale) {
		List<Data> data = new ArrayList<Data>();
		for (int i = 0; i < size; i++) {
			data.add(new Data(monthsFull[i % monthsFull.length], Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min)), Math
					.floor(Math.max(Math.random() * scale, min))));
		}
		return data;
	}
}

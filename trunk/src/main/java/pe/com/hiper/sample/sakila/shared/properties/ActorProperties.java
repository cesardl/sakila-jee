/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sample.sakila.shared.properties;

import java.util.Date;

import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * 
 * @author pdiaz
 */
public interface ActorProperties extends PropertyAccess<Actor> {

	ModelKeyProvider<Actor> actorId();

	ValueProvider<Actor, String> firstName();

	ValueProvider<Actor, String> lastName();

	ValueProvider<Actor, Date> lastUpdate();
}

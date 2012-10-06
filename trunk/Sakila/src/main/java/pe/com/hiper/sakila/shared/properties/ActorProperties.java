/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.shared.properties;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import java.util.Date;
import pe.com.hiper.sakila.shared.dto.ActorDto;

/**
 *
 * @author pdiaz
 */
public interface ActorProperties extends PropertyAccess<ActorDto> {

    ModelKeyProvider<ActorDto> actorId();

    ValueProvider<ActorDto, String> firstName();

    ValueProvider<ActorDto, String> lastName();

    ValueProvider<ActorDto, Date> lastUpdate();
}

package pe.com.hiper.sample.sakila.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface AppConstants extends Constants {

	public static AppConstants msgs = GWT.create(AppConstants.class);

	public String app_name();

	public String maintance();

	public String reports();

	public String lan_english();

	public String lan_spanish();

	public String actors();

	public String films();

	public String name();

	public String first_name();

	public String last_name();

	public String last_update();

	public String search();

	public String enter_name();

	public String enter_last_name();

	public String new_actor();
	
	public String charts();

}

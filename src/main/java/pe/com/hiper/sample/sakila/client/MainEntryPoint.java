/*
O * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sample.sakila.client;

import pe.com.hiper.sample.sakila.client.gui.BaseLayout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * Main entry point.
 * 
 * @author pdiaz
 */
public class MainEntryPoint implements EntryPoint {

	private LocaleInfo currentLocale;

	/**
	 * Creates a new instance of MainEntryPoint
	 */
	public MainEntryPoint() {
		currentLocale = LocaleInfo.getCurrentLocale();
	}

	/**
	 * The entry point method, called automatically by loading a module that
	 * declares an implementing class as an entry-point
	 */
	public void onModuleLoad() {
		final SimpleComboBox<String> language = new SimpleComboBox<String>(
				new StringLabelProvider<String>());
		language.setTriggerAction(TriggerAction.ALL);
		language.setEditable(false);
		language.setWidth(150);
		language.add(AppConstants.msgs.lan_english());
		language.add(AppConstants.msgs.lan_spanish());

		if (currentLocale.getLocaleName().equals("es")) {
			language.setValue(AppConstants.msgs.lan_spanish());
		} else {
			language.setValue(AppConstants.msgs.lan_english());
		}

		language.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				String lan = "";
				if (event.getSelectedItem().equals(
						AppConstants.msgs.lan_english())) {
					lan = "en";
				} else if (event.getSelectedItem().equals(
						AppConstants.msgs.lan_spanish())) {
					lan = "es";
				}
				Window.Location.assign(Window.Location.createUrlBuilder()
						.setParameter(LocaleInfo.getLocaleQueryParam(), lan)
						.buildString());
			}
		});

		Widget con = new BaseLayout(language).asWidget();
		Viewport viewport = new Viewport();
		viewport.add(con);
		RootPanel.get().add(viewport);
	}
}

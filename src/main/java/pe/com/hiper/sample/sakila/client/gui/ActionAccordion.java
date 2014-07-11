package pe.com.hiper.sample.sakila.client.gui;

import pe.com.hiper.sample.sakila.client.AppConstants;
import pe.com.hiper.sample.sakila.client.TestData;
import pe.com.hiper.sample.sakila.client.model.NamedModel;
import pe.com.hiper.sample.sakila.client.model.NamedModel.NamedModelProperties;
import pe.com.hiper.sample.sakila.client.model.images.ExampleImages;
import pe.com.hiper.sample.sakila.client.model.impl.TaskModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class ActionAccordion implements IsWidget {

	private ContentPanel panel;
	private ListStore<NamedModel> store;
	private ListView<NamedModel, String> list;

	public ActionAccordion() {
		NamedModelProperties props = GWT.create(NamedModelProperties.class);

		store = new ListStore<NamedModel>(props.kp());
		store.add(new TaskModel(AppConstants.msgs.actors(), TestData.TYPE_ACTOR));
		store.add(new TaskModel(AppConstants.msgs.films(), TestData.TYPE_FILM));
		store.add(new TaskModel(AppConstants.msgs.charts(),
				TestData.TYPE_CHART_COLUMN));

		list = new ListView<NamedModel, String>(store, props.name());
		list.setBorders(false);

	}

	@Override
	public Widget asWidget() {
		if (panel == null) {
			panel = new ContentPanel();
			panel.setHeaderVisible(false);
			panel.setBodyBorder(false);
			panel.setBorders(false);
			panel.getHeader().setIcon(ExampleImages.INSTANCE.accordion());

			AccordionLayoutContainer accordion = new AccordionLayoutContainer();
			accordion.setExpandMode(ExpandMode.SINGLE_FILL);
			accordion.setBorders(false);
			panel.add(accordion);

			AccordionLayoutAppearance appearance = GWT
					.<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);

			ContentPanel cp = new ContentPanel(appearance);
			cp.setAnimCollapse(false);
			cp.setBodyBorder(false);
			cp.setHeadingText(AppConstants.msgs.maintance());
			cp.add(list);
			accordion.add(cp);

			accordion.setActiveWidget(cp);
		}
		return panel;
	}

	public void addSelectionChangedHandler(
			SelectionChangedHandler<NamedModel> selectHandler) {
		list.getSelectionModel().addSelectionChangedHandler(selectHandler);
	}

}

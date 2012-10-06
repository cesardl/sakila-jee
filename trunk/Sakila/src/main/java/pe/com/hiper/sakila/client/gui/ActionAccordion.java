package pe.com.hiper.sakila.client.gui;

import java.util.Comparator;
import java.util.List;

import pe.com.hiper.sakila.client.AppConstants;
import pe.com.hiper.sakila.client.model.NamedModel;
import pe.com.hiper.sakila.client.model.NamedModel.NamedModelProperties;
import pe.com.hiper.sakila.client.model.images.ExampleImages;
import pe.com.hiper.sakila.client.model.impl.Task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class ActionAccordion implements IsWidget {

	private ContentPanel panel;
	private ListStore<NamedModel> listStore;
	private ListView<NamedModel, String> list;

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
			cp.add(getList());

			accordion.add(cp);
			accordion.setActiveWidget(cp);
		}
		return panel;
	}

	public ListView<NamedModel, String> getList() {
		NamedModelProperties props = GWT.create(NamedModelProperties.class);

		listStore = new ListStore<NamedModel>(props.kp());
		listStore.addSortInfo(new StoreSortInfo<NamedModel>(
				new Comparator<NamedModel>() {
					@Override
					public int compare(NamedModel o1, NamedModel o2) {
						return o1.getName().compareTo(o2.getName());
					}
				}, SortDir.ASC));
		listStore.add(new Task(AppConstants.msgs.actors()));
		listStore.add(new Task(AppConstants.msgs.films()));

		list = new ListView<NamedModel, String>(listStore, props.name());
		list.setBorders(false);
		list.getSelectionModel().addSelectionChangedHandler(selectHandler);

		return list;
	}

	SelectionChangedHandler<NamedModel> selectHandler = new SelectionChangedHandler<NamedModel>() {
		@Override
		public void onSelectionChanged(SelectionChangedEvent<NamedModel> event) {
			List<NamedModel> sels = event.getSelection();
			if (sels.size() > 0) {
				NamedModel m = sels.get(0);
				Info.display(AppConstants.msgs.app_name(), m.getName());
			}
		}
	};
}

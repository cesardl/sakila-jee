package pe.com.hiper.sample.sakila.client.gui;

import java.util.List;

import pe.com.hiper.sample.sakila.client.AppConstants;
import pe.com.hiper.sample.sakila.client.TestData;
import pe.com.hiper.sample.sakila.client.gui.chart.ColumnExample;
import pe.com.hiper.sample.sakila.client.gui.chart.GaugeExample;
import pe.com.hiper.sample.sakila.client.model.NamedModel;
import pe.com.hiper.sample.sakila.client.model.images.ExampleImages;
import pe.com.hiper.sample.sakila.client.model.impl.TaskModel;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class BaseLayout implements IsWidget {

	private Widget widget;
	private ActionAccordion accordion;
	private ContentPanel center;

	public BaseLayout(Widget widget) {
		this.widget = widget;
		this.accordion = new ActionAccordion();
	}

	@Override
	public Widget asWidget() {
		accordion.addSelectionChangedHandler(selectHandler);

		final BorderLayoutContainer con = new BorderLayoutContainer();
		con.setBorders(true);

		ContentPanel north = new ContentPanel();
		ContentPanel west = new ContentPanel();
		center = new ContentPanel();

		north.setHeaderVisible(false);
		north.add(getHeader());

		west.setHeaderVisible(false);
		west.add(accordion);

		center.setHeadingText(AppConstants.msgs.app_name());
		center.getHeader().setIcon(ExampleImages.INSTANCE.table());
		center.setResize(false);

		BorderLayoutData northData = new BorderLayoutData(100);
		northData.setMargins(new Margins(5));
		northData.setCollapsible(true);
		northData.setSplit(true);

		BorderLayoutData westData = new BorderLayoutData(150);
		westData.setCollapsible(true);
		westData.setSplit(true);
		westData.setCollapseMini(true);
		westData.setMargins(new Margins(0, 5, 5, 5));

		MarginData centerData = new MarginData(0, 5, 5, 0);

		con.setNorthWidget(north, northData);
		con.setWestWidget(west, westData);
		con.setCenterWidget(center, centerData);

		SimpleContainer simple = new SimpleContainer();
		simple.add(con, new MarginData(10));

		return simple;
	}

	SelectionChangedHandler<NamedModel> selectHandler = new SelectionChangedHandler<NamedModel>() {
		@Override
		public void onSelectionChanged(SelectionChangedEvent<NamedModel> event) {
			List<NamedModel> sels = event.getSelection();
			if (sels.size() > 0) {
				TaskModel m = (TaskModel) sels.get(0);

				center.clear();
				switch (m.getType()) {
				case TestData.TYPE_ACTOR:
					center.add(new ActorPagingGrid());
					break;

				case TestData.TYPE_FILM:
					center.add(new Label("Films " + m.getName() + " "
							+ m.getType()));
					break;

				case TestData.TYPE_CHART_COLUMN:
					HorizontalLayoutContainer hl = new HorizontalLayoutContainer();
					hl.add(new ColumnExample().asWidget());
					hl.add(new GaugeExample().asWidget());
					center.add(hl);
					break;
				}
			}
		}
	};

	public Widget getHeader() {
		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.TOP);

		Image image = new Image(ExampleImages.INSTANCE.logoHiper());
		c.add(image, new BoxLayoutData(new Margins(0, 5, 0, 0)));
		BoxLayoutData flex = new BoxLayoutData(new Margins(0, 5, 0, 5));
		flex.setFlex(1);
		c.add(new Label(), flex);
		c.add(widget, new BoxLayoutData(new Margins(5)));

		return c;
	}

}

package pe.com.hiper.sakila.client.gui;

import pe.com.hiper.sakila.client.AppConstants;
import pe.com.hiper.sakila.client.model.images.ExampleImages;

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
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

public class BaseLayout implements IsWidget {

	private Widget widget;

	public BaseLayout(Widget widget) {
		this.widget = widget;
	}

	@Override
	public Widget asWidget() {
		final BorderLayoutContainer con = new BorderLayoutContainer();
		con.setBorders(true);

		ContentPanel north = new ContentPanel();
		ContentPanel west = new ContentPanel();
		ContentPanel center = new ContentPanel();

		north.setHeaderVisible(false);
		north.add(getHeader());

		west.setHeaderVisible(false);
		west.add(new ActionAccordion());

		center.setHeadingText(AppConstants.msgs.app_name());
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

	public Widget getHeader() {
		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.TOP);

		c.add(new Image(ExampleImages.INSTANCE.logoHiper()), new BoxLayoutData(
				new Margins(0, 5, 0, 0)));
		BoxLayoutData flex = new BoxLayoutData(new Margins(0, 5, 0, 5));
		flex.setFlex(1);
		c.add(new Label(), flex);
		c.add(widget, new BoxLayoutData(new Margins(5)));

		return c;
	}
}

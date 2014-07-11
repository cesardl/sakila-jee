/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package pe.com.hiper.sample.sakila.client.gui.chart;

import pe.com.hiper.sample.sakila.client.TestData;
import pe.com.hiper.sample.sakila.client.model.Data;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.axis.GaugeAxis;
import com.sencha.gxt.chart.client.chart.series.GaugeSeries;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.easing.Default;
import com.sencha.gxt.fx.client.easing.EasingFunction;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class GaugeExample implements IsWidget {

	public interface DataPropertyAccess extends PropertyAccess<Data> {
		ValueProvider<Data, Double> data1();

		ValueProvider<Data, Double> data2();

		ValueProvider<Data, Double> data3();

		ValueProvider<Data, String> name();

		@Path("name")
		ModelKeyProvider<Data> nameKey();
	}

	private static final DataPropertyAccess dataAccess = GWT
			.create(DataPropertyAccess.class);

	@Override
	public Widget asWidget() {
		final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
		store.addAll(TestData.getData(1, 0, 100));

		final Chart<Data> chart2 = createGauge(store, 30, new RGB("#82B525"),
				false, new Default(), dataAccess.data2());

		TextButton regenerate = new TextButton("Reload Data");
		regenerate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				store.clear();
				store.addAll(TestData.getData(1, 0, 100));
				chart2.redrawChart();
			}
		});

		ToggleButton animation = new ToggleButton("Animate");
		animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				chart2.setAnimated(event.getValue());
			}
		});
		animation.setValue(true, true);

		ToolBar toolBar = new ToolBar();
		toolBar.add(regenerate);
		toolBar.add(animation);

		ContentPanel panel = new FramedPanel();
		panel.getElement().getStyle().setMargin(10, Unit.PX);
		panel.setCollapsible(true);
		panel.setHeadingText("Gauge Chart");
		panel.setPixelSize(420, 420);
		panel.setBodyBorder(true);

		VerticalLayoutContainer layout = new VerticalLayoutContainer();
		panel.add(layout);

		toolBar.setLayoutData(new VerticalLayoutData(1, -1));
		layout.add(toolBar);

		chart2.setLayoutData(new VerticalLayoutData(400, 200));
		layout.add(chart2);

		return panel;
	}

	private Chart<Data> createGauge(ListStore<Data> store, double donut,
			Color color, boolean needle, EasingFunction easing,
			ValueProvider<Data, Double> provider) {
		Chart<Data> chart = new Chart<Data>();
		chart.setStore(store);
		chart.setAnimationDuration(750);
		chart.setAnimationEasing(easing);
		chart.setDefaultInsets(20);

		GaugeAxis<Data> axis = new GaugeAxis<Data>();
		axis.setMargin(8);
		axis.setDisplayGrid(true);
		axis.setMinimum(0);
		axis.setMaximum(100);
		chart.addAxis(axis);

		final GaugeSeries<Data> gauge = new GaugeSeries<Data>();
		gauge.addColor(color);
		gauge.addColor(new RGB("#ddd"));
		gauge.setAngleField(provider);
		gauge.setNeedle(needle);
		gauge.setDonut(donut);
		chart.addSeries(gauge);

		return chart;
	}
}

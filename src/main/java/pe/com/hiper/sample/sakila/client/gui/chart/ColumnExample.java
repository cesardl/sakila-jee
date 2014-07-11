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
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class ColumnExample implements IsWidget {

	public interface DataPropertyAccess extends PropertyAccess<Data> {
		ValueProvider<Data, Double> data1();

		ValueProvider<Data, String> name();

		@Path("name")
		ModelKeyProvider<Data> nameKey();
	}

	private static final DataPropertyAccess dataAccess = GWT
			.create(DataPropertyAccess.class);

	@Override
	public Widget asWidget() {
		final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
		store.addAll(TestData.getData(12, 0, 10000));

		final Chart<Data> chart = new Chart<Data>();
		chart.setStore(store);
		chart.setShadowChart(true);

		NumericAxis<Data> axis = new NumericAxis<Data>();
		axis.setPosition(Position.LEFT);
		axis.addField(dataAccess.data1());
		TextSprite title = new TextSprite("Number of Hits");
		title.setFontSize(18);
		axis.setTitleConfig(title);
		axis.setDisplayGrid(true);
		axis.setWidth(50);
		chart.addAxis(axis);

		CategoryAxis<Data, String> catAxis = new CategoryAxis<Data, String>();
		catAxis.setPosition(Position.BOTTOM);
		catAxis.setField(dataAccess.name());
		title = new TextSprite("Month of the Year");
		title.setFontSize(18);
		catAxis.setTitleConfig(title);
		catAxis.setLabelProvider(new LabelProvider<String>() {
			@Override
			public String getLabel(String item) {
				return item.substring(0, 3);
			}
		});
		chart.addAxis(catAxis);

		final BarSeries<Data> column = new BarSeries<Data>();
		column.setYAxisPosition(Position.LEFT);
		column.addYField(dataAccess.data1());
		column.addColor(new RGB(148, 174, 10));
		column.setColumn(true);
		chart.addSeries(column);

		TextButton regenerate = new TextButton("Reload Data");
		regenerate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				store.clear();
				store.addAll(TestData.getData(12, 0, 10000));
				chart.redrawChart();
			}
		});

		ToggleButton animation = new ToggleButton("Animate");
		animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				chart.setAnimated(event.getValue());
			}
		});
		animation.setValue(true, true);
		ToggleButton shadow = new ToggleButton("Shadow");
		shadow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				chart.setShadowChart(event.getValue());
				chart.redrawChart();
			}
		});
		shadow.setValue(true);

		ToolBar toolBar = new ToolBar();
		toolBar.add(regenerate);
		toolBar.add(animation);
		toolBar.add(shadow);

		ContentPanel panel = new FramedPanel();
		panel.getElement().getStyle().setMargin(10, Unit.PX);
		panel.setCollapsible(true);
		panel.setHeadingText("Column Chart");
		panel.setPixelSize(420, 420);

		panel.setBodyBorder(true);

		final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
		resize.setMinHeight(400);
		resize.setMinWidth(400);

//		panel.addExpandHandler(new ExpandHandler() {
//			@Override
//			public void onExpand(ExpandEvent event) {
//				resize.setEnabled(true);
//			}
//		});
		panel.addCollapseHandler(new CollapseHandler() {
			@Override
			public void onCollapse(CollapseEvent event) {
				resize.setEnabled(false);
			}
		});

		new Draggable(panel, panel.getHeader()).setUseProxy(false);

		VerticalLayoutContainer layout = new VerticalLayoutContainer();
		panel.add(layout);

		toolBar.setLayoutData(new VerticalLayoutData(1, -1));
		layout.add(toolBar);

		chart.setLayoutData(new VerticalLayoutData(1, 1));
		layout.add(chart);

		return panel;
	}

}

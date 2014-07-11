/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sample.sakila.client.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.hiper.sample.sakila.client.AppConstants;
import pe.com.hiper.sample.sakila.client.service.ActorService;
import pe.com.hiper.sample.sakila.client.service.ActorServiceAsync;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Actor;
import pe.com.hiper.sample.sakila.shared.properties.ActorProperties;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

/**
 * 
 * @author pdiaz
 */
public class ActorPagingGrid implements IsWidget {

	private TextField firstName;
	private TextField lastName;

	public Widget asWidget() {
		final ActorServiceAsync service = GWT.create(ActorService.class);

		RpcProxy<PagingLoadConfig, PagingLoadResult<Actor>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Actor>>() {

			@Override
			public void load(PagingLoadConfig loadConfig,
					AsyncCallback<PagingLoadResult<Actor>> callback) {
				service.getActors(loadConfig, firstName.getCurrentValue(),
						lastName.getCurrentValue(), callback);
			}
		};

		ActorProperties props = GWT.create(ActorProperties.class);

		ListStore<Actor> store = new ListStore<Actor>(
				new ModelKeyProvider<Actor>() {

					@Override
					public String getKey(Actor item) {
						return item.getActorId().toString();
					}
				});

		final PagingLoader<PagingLoadConfig, PagingLoadResult<Actor>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Actor>>(
				proxy);
		loader.setRemoteSort(true);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Actor, PagingLoadResult<Actor>>(
				store));

		final PagingToolBar toolBar = new PagingToolBar(20);
		toolBar.getElement().getStyle().setProperty("borderBottom", "none");
		toolBar.bind(loader);

		IdentityValueProvider<Actor> identity = new IdentityValueProvider<Actor>();
		RowNumberer<Actor> numberer = new RowNumberer<Actor>(identity);

		ColumnConfig<Actor, String> firstNameColumn = new ColumnConfig<Actor, String>(
				props.firstName());
		firstNameColumn.setHeader(AppConstants.msgs.first_name());
		ColumnConfig<Actor, String> lastNameColumn = new ColumnConfig<Actor, String>(
				props.lastName());
		lastNameColumn.setHeader(AppConstants.msgs.last_name());
		ColumnConfig<Actor, Date> lastUpdateColumn = new ColumnConfig<Actor, Date>(
				props.lastUpdate());
		lastUpdateColumn.setHeader(AppConstants.msgs.last_update());
		lastUpdateColumn.setCell(new DateCell(DateTimeFormat
				.getFormat(PredefinedFormat.DATE_SHORT)));

		List<ColumnConfig<Actor, ?>> l = new ArrayList<ColumnConfig<Actor, ?>>();
		l.add(numberer);
		l.add(firstNameColumn);
		l.add(lastNameColumn);
		l.add(lastUpdateColumn);

		ColumnModel<Actor> cm = new ColumnModel<Actor>(l);
		GridSelectionModel<Actor> sm = new GridSelectionModel<Actor>();
		sm.setSelectionMode(SelectionMode.SINGLE);

		Grid<Actor> grid = new Grid<Actor>(store, cm);
		grid.setSelectionModel(sm);
		grid.getSelectionModel().addSelectionHandler(
				new SelectionHandler<Actor>() {

					@Override
					public void onSelection(SelectionEvent<Actor> event) {
						Actor dto = event.getSelectedItem();
						Info.display(AppConstants.msgs.app_name(),
								dto.getFirstName() + " " + dto.getLastName());
					}
				});
		grid.getView().setForceFit(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.setLoadMask(true);
		grid.setLoader(loader);

		final VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.addStyleName("padding-10");

		TextButton showData = new TextButton(AppConstants.msgs.search());
		showData.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						loader.load();
						container.onResize();
					}
				});
			}
		});

		container.setBorders(false);
		container.add(filterForm());
		container.add(showData);
		container.add(toolBar, new VerticalLayoutData(1, -1));
		container.add(grid, new VerticalLayoutData(1, 1));

		return container;
	}

	public VerticalLayoutContainer filterForm() {
		VerticalLayoutContainer p = new VerticalLayoutContainer();
		firstName = new TextField();
		firstName.setAllowBlank(false);
		firstName.setEmptyText(AppConstants.msgs.enter_name());

		p.add(new FieldLabel(firstName, AppConstants.msgs.name()),
				new VerticalLayoutData(1, -1));

		lastName = new TextField();
		lastName.setAllowBlank(false);
		lastName.setEmptyText(AppConstants.msgs.enter_last_name());

		p.add(new FieldLabel(lastName, AppConstants.msgs.last_name()),
				new VerticalLayoutData(1, -1));

		return p;
	}
}

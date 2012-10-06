/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.com.hiper.sakila.client.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.hiper.sakila.client.AppConstants;
import pe.com.hiper.sakila.client.service.ActorService;
import pe.com.hiper.sakila.client.service.ActorServiceAsync;
import pe.com.hiper.sakila.shared.dto.ActorDto;
import pe.com.hiper.sakila.shared.properties.ActorProperties;

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

		RpcProxy<PagingLoadConfig, PagingLoadResult<ActorDto>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<ActorDto>>() {

			@Override
			public void load(PagingLoadConfig loadConfig,
					AsyncCallback<PagingLoadResult<ActorDto>> callback) {
				service.getActors(loadConfig, firstName.getCurrentValue(),
						lastName.getCurrentValue(), callback);
			}
		};

		AsyncCallback<ActorDto> c = new AsyncCallback<ActorDto>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ActorDto result) {
				// TODO Auto-generated method stub

			}
		};
		service.findActor(null, c);
		ActorProperties props = GWT.create(ActorProperties.class);

		ListStore<ActorDto> store = new ListStore<ActorDto>(
				new ModelKeyProvider<ActorDto>() {

					@Override
					public String getKey(ActorDto item) {
						return item.getActorId().toString();
					}
				});

		final PagingLoader<PagingLoadConfig, PagingLoadResult<ActorDto>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<ActorDto>>(
				proxy);
		loader.setRemoteSort(true);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, ActorDto, PagingLoadResult<ActorDto>>(
				store));

		final PagingToolBar toolBar = new PagingToolBar(20);
		toolBar.getElement().getStyle().setProperty("borderBottom", "none");
		toolBar.bind(loader);

		IdentityValueProvider<ActorDto> identity = new IdentityValueProvider<ActorDto>();
		RowNumberer<ActorDto> numberer = new RowNumberer<ActorDto>(identity);

		ColumnConfig<ActorDto, String> firstNameColumn = new ColumnConfig<ActorDto, String>(
				props.firstName());
		firstNameColumn.setHeader(AppConstants.msgs.first_name());
		ColumnConfig<ActorDto, String> lastNameColumn = new ColumnConfig<ActorDto, String>(
				props.lastName());
		lastNameColumn.setHeader(AppConstants.msgs.last_name());
		ColumnConfig<ActorDto, Date> lastUpdateColumn = new ColumnConfig<ActorDto, Date>(
				props.lastUpdate());
		lastUpdateColumn.setHeader(AppConstants.msgs.last_update());
		lastUpdateColumn.setCell(new DateCell(DateTimeFormat
				.getFormat(PredefinedFormat.DATE_SHORT)));

		List<ColumnConfig<ActorDto, ?>> l = new ArrayList<ColumnConfig<ActorDto, ?>>();
		l.add(numberer);
		l.add(firstNameColumn);
		l.add(lastNameColumn);
		l.add(lastUpdateColumn);

		ColumnModel<ActorDto> cm = new ColumnModel<ActorDto>(l);
		GridSelectionModel<ActorDto> sm = new GridSelectionModel<ActorDto>();
		sm.setSelectionMode(SelectionMode.SINGLE);

		Grid<ActorDto> grid = new Grid<ActorDto>(store, cm);
		grid.setSelectionModel(sm);
		grid.getSelectionModel().addSelectionHandler(
				new SelectionHandler<ActorDto>() {

					@Override
					public void onSelection(SelectionEvent<ActorDto> event) {
						ActorDto dto = event.getSelectedItem();
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

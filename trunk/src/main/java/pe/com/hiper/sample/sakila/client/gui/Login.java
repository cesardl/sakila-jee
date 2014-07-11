package pe.com.hiper.sample.sakila.client.gui;

import pe.com.hiper.sample.sakila.client.service.StaffService;
import pe.com.hiper.sample.sakila.client.service.StaffServiceAsync;
import pe.com.hiper.sample.sakila.shared.domain.mapping.Staff;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

public class Login implements IsWidget {

	private TextField usuario;
	private PasswordField contrasenia;
	private TextButton aceptar;
	private TextButton cancelar;

	@Override
	public Widget asWidget() {
		final StaffServiceAsync service = GWT.create(StaffService.class);

		final AsyncCallback<Staff> callback = new AsyncCallback<Staff>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Staff result) {
				System.out.println(result.getEmail());
			}
		};
		service.get("admin", "123456789", callback);

		final HideHandler hideHandler = new HideHandler() {
			@Override
			public void onHide(HideEvent event) {
				Dialog btn = (Dialog) event.getSource();
				String msg = Format.substitute("The '{0}' button was pressed",
						btn.getHideButton().getText());
				Info.display("MessageBox", msg);
			}
		};

		CenterLayoutContainer con = new CenterLayoutContainer();

		FramedPanel panel = new FramedPanel();
		panel.setHeadingText("Inicio de Sesión");
		panel.setHeaderVisible(true);
		panel.setButtonAlign(BoxLayoutPack.END);
		panel.setWidth(350);

		FieldSet fieldSet = new FieldSet();
		fieldSet.setHeadingText("Datos del Usuario");
		fieldSet.setCollapsible(true);
		panel.add(fieldSet);

		VerticalLayoutContainer p = new VerticalLayoutContainer();
		fieldSet.add(p);

		usuario = new TextField();
		usuario.setAllowBlank(false);
		p.add(new FieldLabel(usuario, "Usuario"), new VerticalLayoutData(1, -1));

		contrasenia = new PasswordField();
		contrasenia.setAllowBlank(false);
		p.add(new FieldLabel(contrasenia, "Contraseña"),
				new VerticalLayoutData(1, -1));

		aceptar = new TextButton("Aceptar");
		aceptar.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				System.out.println(usuario.getCurrentValue() + " - "
						+ contrasenia.getCurrentValue());

				if (usuario != null
						&& usuario.getCurrentValue().trim().length() == 0) {
					// Info.display("BMatic", "Valor vacio");
					AlertMessageBox alerta = new AlertMessageBox("Alerta",
							"Campo Vacio");
					alerta.addHideHandler(hideHandler);
					alerta.show();
				} else {
					// String strNombre = usuario.getCurrentValue();
					// String strPassword = contraseña.getCurrentValue();
					// service.get(strNombre, strPassword, callback);
				}
			}
		});

		cancelar = new TextButton("Cancelar");

		panel.addButton(aceptar);
		panel.addButton(cancelar);

		con.add(panel);
		return con;
	}
}

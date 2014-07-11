package pe.com.hiper.sample.sakila.client.service;

import pe.com.hiper.sample.sakila.shared.domain.mapping.Staff;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/staffService")
public interface StaffService extends RemoteService {

	public Staff get(String nombre, String password);
}

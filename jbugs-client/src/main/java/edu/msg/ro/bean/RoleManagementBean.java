package edu.msg.ro.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import edu.msg.ro.business.role.service.RoleService;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.role.entity.RoleName;

@ManagedBean
@ViewScoped
public class RoleManagementBean implements Serializable {

	@EJB
	private RoleService roleService;

	private List<RoleName> roles;

	private List<PermissionType> permissions;

	Map<RoleName, List<PermissionType>> rolePermissions;

	Map<RoleName, Map<PermissionType, Boolean>> configuration;

	public Map<RoleName, List<PermissionType>> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(Map<RoleName, List<PermissionType>> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

	@PostConstruct
	public void init() {
		configuration = roleService.getConfiguration();
		permissions = Arrays.asList(PermissionType.PERMISSON_MANAGEMENT, PermissionType.USER_MANAGEMENT,
				PermissionType.BUG_MANAGEMENT, PermissionType.BUG_CLOSE, PermissionType.BUG_EXPORT_PDF);
		rolePermissions = roleService.getRolePermissions();
		roles = Arrays.asList(RoleName.ADM, RoleName.DEV, RoleName.PM, RoleName.TEST, RoleName.TM);
	}

	public List<RoleName> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleName> roles) {
		this.roles = roles;
	}

	public List<PermissionType> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionType> permissions) {
		this.permissions = permissions;
	}

	public Boolean hasPermission(RoleName name, PermissionType type) {
		return rolePermissions.get(name).contains(type);
	}

	public void updateMap(RoleName name, PermissionType type) {
		System.out.println("In update map");

		if (rolePermissions.get(name).contains(type)) {
			rolePermissions.get(name).remove(type);
		} else {
			rolePermissions.get(name).add(type);
		}
	}

	public Map<RoleName, Map<PermissionType, Boolean>> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<RoleName, Map<PermissionType, Boolean>> configuration) {
		this.configuration = configuration;
	}

	public void saveConfiguration(ActionEvent actionEvent) {
		ResourceBundle messages = ResourceBundle.getBundle("jbugs.messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		try {
			roleService.saveConfig(configuration);
			FacesMessage msg = new FacesMessage(messages.getString("role.management.bean.saveConfiguration.success"),
					"");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					messages.getString("role.management.bean.saveConfiguration.fail"), "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

}

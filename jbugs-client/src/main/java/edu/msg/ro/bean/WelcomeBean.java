package edu.msg.ro.bean;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import edu.msg.ro.business.bug.service.BugService;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.role.entity.RoleName;

@ManagedBean
@SessionScoped
public class WelcomeBean {

	@EJB
	private UserService userService;

	@EJB
	private BugService bugService;

	private boolean hasRoleManagement;
	
	private boolean hasUserManagement;
	
	private String username;

	private UserDTO user;

	private String page = "welcome.xhtml";

	public String getPage() {
		return page;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public void setPage(String currentPage) {
		this.page = currentPage;
	}

	public String getUsername() {
		return username;
	}

	@PostConstruct
	public void init() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		username = (String) session.getAttribute("username");
		user = (UserDTO) session.getAttribute("user");
		hasRoleManagement = userService.hasPermission(username, PermissionType.PERMISSON_MANAGEMENT);
		hasUserManagement = userService.hasPermission(username, PermissionType.USER_MANAGEMENT);
	}

	public String goToUsersPage() {
		if (userService.hasPermission(username, PermissionType.USER_MANAGEMENT)) {
			return "users.xhtml/?faces-redirect=true";
		}
		return "welcome.xhtml/?faces-redirect=true";
	}

	public String goToBugsPage() {
		return "bugs.xhtml/?faces-redirect=true";
	}

	public String goToRolesPage() {
		if (userService.hasPermission(username, PermissionType.PERMISSON_MANAGEMENT)
				|| userService.hasRole(username, RoleName.ADM)) {
			return "roles.xhtml/?faces-redirect=true";
		}
		return "welcome.xhtml/?faces-redirect=true";
	}

	public String goToStatisticsPage(){
		return "statistics.xhtml/?faces-redirect=true";
	}
	
	public String goToNotificationsPage() {
		return "notifications.xhtml/?faces-redirect=true";

	}

	public String goBack() {
		init();
		return "welcome.xhtml/?faces-redirect=true";
	}

	public boolean isHasRoleManagement() {
		return hasRoleManagement;
	}

	public void setHasRoleManagement(boolean hasRoleManagement) {
		this.hasRoleManagement = hasRoleManagement;
	}

	public boolean isHasUserManagement() {
		return hasUserManagement;
	}

	public void setHasUserManagement(boolean hasUserManagement) {
		this.hasUserManagement = hasUserManagement;
	}
	
	

}

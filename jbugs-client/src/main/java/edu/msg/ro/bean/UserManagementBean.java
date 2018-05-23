package edu.msg.ro.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import edu.msg.ro.business.bug.service.BugService;
import edu.msg.ro.business.exception.JBugsBusinessException;
import edu.msg.ro.business.notification.service.NotificationMessageGenerator;
import edu.msg.ro.business.notification.service.NotificationService;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.dto.mapper.UserDTOMapper;
import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.role.dao.RoleDao;
import edu.msg.ro.persistence.role.entity.RoleName;
import edu.msg.ro.persistence.user.dao.UserDao;
import edu.msg.ro.persistence.user.entity.NotificationType;
import edu.msg.ro.persistence.user.entity.User;

@ManagedBean
@ViewScoped
public class UserManagementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8195978446996520723L;

	private String username;

	@EJB
	private UserService userService;

	@EJB
	private NotificationService notificationService;

	@EJB
	private UserDao userDao;

	@EJB
	private RoleDao roleDao;

	@EJB
	private UserDTOMapper userMapper;

	@EJB
	private BugService bugService;

	private UserDTO newUser = new UserDTO();

	private List<UserDTO> users;

	private List<String> roles;

	private PieChartModel userBugStatistics;

	private String selectedUserUsername;

	private List<UserDTO> filteredUsers;

	@EJB
	UserDTOMapper userDTOMapper;

	@EJB
	NotificationMessageGenerator messageGenerator;

	@PostConstruct
	public void init() {
		users = userService.getAllUsers();

		roles = Stream.of(RoleName.values()).map(RoleName::name).collect(Collectors.toList());
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		username = (String) session.getAttribute("username");
		for(UserDTO userDTO:users){
			if(userDTO.getUsername().equals(username)){
				users.remove(userDTO);
				break;
			}
		}
	}

	public List<UserDTO> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<UserDTO> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	private Long editedUserId;

	public Long getEditedUserId() {
		return editedUserId;
	}

	public void setEditedUserId(Long editedUserId) {
		this.editedUserId = editedUserId;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}

	public UserDTO getNewUser() {
		return newUser;
	}

	public void setNewUser(UserDTO newUser) {
		this.newUser = newUser;
	}

	public List<UserDTO> getAllusers() {
		return userService.getAllUsers();
	}

	public PieChartModel getUserBugStatistics() {
		return userBugStatistics;
	}

	public void setUserBugStatistics(PieChartModel userBugStatistics) {
		this.userBugStatistics = userBugStatistics;
	}

	public String getSelectedUserUsername() {
		return selectedUserUsername;
	}

	public void setSelectedUserUsername(String selectedUserUsername) {
		this.selectedUserUsername = selectedUserUsername;
	}

	public String activateUser(Long id) {
		try {
			userService.activateUser(id);
			userService.deleteHistory(id);
			List<User> admins = userService.getUsersByRole(RoleName.ADM);
			notificationService.addUserStatusNotification(id, NotificationType.USER_ACTIVATED, admins);
			User user = userService.findById(id);
			String summary = "User status updated!";
			String detail = messageGenerator.generate(userDTOMapper.mapToDTO(user), NotificationType.USER_ACTIVATED);
			String channel = "/notify/";
			admins.forEach(admin -> pushNotification(summary, detail, channel + admin.getId()));
			return "users.xhtml/?faces-redirect=true";
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
			return "";
		}
	}

	public String deactivateUser(Long id) {
		try {
			userService.deactivateUser(id);
			List<User> admins = userService.getUsersByRole(RoleName.ADM);
			notificationService.addUserStatusNotification(id, NotificationType.USER_DEACTIVATED, admins);
			User user = userService.findById(id);
			String summary = "User status updated!";
			String detail = messageGenerator.generate(userDTOMapper.mapToDTO(user), NotificationType.USER_DEACTIVATED);
			String channel = "/notify/";
			admins.forEach(admin -> pushNotification(summary, detail, channel + admin.getId()));
			return "users.xhtml/?faces-redirect=true";
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
			return "";
		}
	}

	public String getSessionUsername() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return (String) session.getAttribute("username");
	}

	public void onRowEdit(RowEditEvent event) {
		try {
			UserDTO selectedUser = (UserDTO) event.getObject();
			User user = userMapper.mapToEntity(selectedUser);
			UserDTO adminDTO = userService.getUserByUsername(username);

			setEditedUserId(selectedUser.getId());

			UserDTO oldUser = userDTOMapper.mapToDTO(userService.findById(getEditedUserId()));
			if (!selectedUser.getEmail().equals(oldUser.getEmail())
					&& userService.checkIfEmailExists(selectedUser.getEmail())) {
				resetSelectedUser(selectedUser, oldUser);
				UserManagementBean.showMessage("users.newUserForm.emailInUse");
			} else {
				userService.updateUser(selectedUser);
				String summary = "User info updated!";
				String detail = messageGenerator.generate(oldUser, userDTOMapper.mapToDTO(user));
				String channel = "/notify/" + getEditedUserId();
				pushNotification(summary, detail, channel);
				notificationService.addUserNotification(NotificationType.USER_UPDATED, selectedUser, adminDTO.getId());
				FacesMessage msg = new FacesMessage("User Edited", ((UserDTO) event.getObject()).getUsername());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
		}
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", ((UserDTO) event.getObject()).getUsername());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String doCreateUser() {
		try {
			if (!userService.checkIfEmailExists(newUser.getEmail())) {
				userService.saveNewUser(newUser.getFirstName(), newUser.getLastName(), newUser.getPhoneNumber(),
						newUser.getEmail(), newUser.getPassword(), newUser.getUserRoles());
				UserManagementBean.showMessage("users.newUserForm.add.success");
				return "users.xhtml/?faces-redirect=true";
			} else
				UserManagementBean.showMessage("users.newUserForm.emailInUse");
			return "users.xhtml/?faces-redirect=false";
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
			return "welcome.xhtml";
		}
	}

	public boolean hasTasks(Long id) {

		if (userService.hasTasks(id).isEmpty())
			return false;
		return true;
	}

	public String goToBugPage() {
		return "bugs.xhtml/?faces-redirect=true";
	}

	public void pushNotification(String summary, String detail, String channel) {
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish(channel, new FacesMessage(summary, detail));
	}

	public String getCurrentId() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session.getAttribute("userId").toString();
	}

	public String getCurrentuser() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session.getAttribute("username").toString();
	}

	public PieChartModel createUserBugStatistics() {
		userBugStatistics = new PieChartModel();
		int x, x1, x2, x3, x4, x5;
		ResourceBundle messages = ResourceBundle.getBundle("jbugs.messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		userBugStatistics.set(messages.getString("bugs.bugForm.status.NEW"),
				x = bugService.getNumberOfBugsByStatusForUser(BugStatusType.NEW, selectedUserUsername));
		userBugStatistics.set(messages.getString("bugs.bugForm.status.IN_PROGRESS"),
				x1 = bugService.getNumberOfBugsByStatusForUser(BugStatusType.IN_PROGRESS, selectedUserUsername));
		userBugStatistics.set(messages.getString("bugs.bugForm.status.INFO_NEEDED"),
				x2 = bugService.getNumberOfBugsByStatusForUser(BugStatusType.INFO_NEEDED, selectedUserUsername));
		userBugStatistics.set(messages.getString("bugs.bugForm.status.REJECTED"),
				x3 = bugService.getNumberOfBugsByStatusForUser(BugStatusType.REJECTED, selectedUserUsername));
		userBugStatistics.set(messages.getString("bugs.bugForm.status.FIXED"),
				x4 = bugService.getNumberOfBugsByStatusForUser(BugStatusType.FIXED, selectedUserUsername));
		userBugStatistics.set(messages.getString("bugs.bugForm.status.CLOSED"),
				x5 = bugService.getNumberOfBugsByStatusForUser(BugStatusType.CLOSED, selectedUserUsername));
		userBugStatistics.setLegendPosition("w");
		userBugStatistics.setShowDataLabels(true);
		if (x == 0 && x1 == 0 && x2 == 0 && x3 == 0 && x4 == 0 && x5 == 0)
			userBugStatistics.setTitle(messages.getString("users.noBugs"));
		return userBugStatistics;
	}

	public static void showMessage(String bundleLabel) {
		ResourceBundle messages = ResourceBundle.getBundle("jbugs.messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		FacesMessage msg = new FacesMessage(messages.getString(bundleLabel), "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	}

	public void resetSelectedUser(UserDTO selectedUser, UserDTO oldUser) {
		selectedUser.setActive(oldUser.isActive());
		selectedUser.setEmail(oldUser.getEmail());
		selectedUser.setFirstName(oldUser.getFirstName());
		selectedUser.setLastName(oldUser.getLastName());
		selectedUser.setPhoneNumber(oldUser.getPhoneNumber());
		selectedUser.setUserRoles(oldUser.getUserRoles());
	}
}

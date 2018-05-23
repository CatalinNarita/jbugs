package edu.msg.ro.bean;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import edu.msg.ro.business.notification.dto.NotificationDTO;
import edu.msg.ro.business.notification.service.NotificationService;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.service.UserService;

@ManagedBean
@ViewScoped
public class NotificationManagementBean {

	private List<NotificationDTO> notifications;
	
	private String message;
	
	private Long userId;

	@EJB
	private NotificationService notificationService;

	@EJB
	private UserService userService;

	@PostConstruct
	public void init() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        userId = (Long) session.getAttribute("userId");
		notifications = userService.getNotifications(userId);
	}

	private UserDTO user = new UserDTO();

	public UserDTO getUserDTO() {
		return user;
	}

	public void setUserDTO(UserDTO user) {
		this.user = user;
	}
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification", message);
		RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
	}

	public List<NotificationDTO> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<NotificationDTO> notifications) {
		this.notifications = notifications;
	}
	
	public String goToLink(String url){
		return url;
	}

}

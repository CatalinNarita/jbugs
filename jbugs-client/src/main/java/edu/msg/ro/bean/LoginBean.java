package edu.msg.ro.bean;

import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.msg.ro.business.exception.JBugsBusinessException;
import edu.msg.ro.business.notification.service.NotificationService;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.service.UserService;

import java.util.ResourceBundle;

@ManagedBean
@RequestScoped
public class LoginBean {

	private boolean isLoggedIn = false;

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	private UserDTO user = new UserDTO();

	@EJB
	private UserService userService;

	@EJB
	private NotificationService notificationService;

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String doLogin() {
		try {
			Long id = null;
			try {
				id = userService.getUserByUsername(user.getUsername()).getId();
			} catch (Exception e){

			}
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle =  ResourceBundle.getBundle("jbugs.messages",context.getViewRoot().getLocale());
			String text = bundle.getString("growl.welcome");
			String newUser = bundle.getString("growl.newUser");
			String newUserSummary = bundle.getString("growl.newUserSummary");

			if (userService.isValidUser(user)) {

				context.getExternalContext().getFlash().setKeepMessages(true);

				FacesMessage msg = new FacesMessage("Login info",text);
				FacesContext.getCurrentInstance().addMessage(null, msg);

				if (userService.checkIfNewUser(user.getUsername())) {
					msg = new FacesMessage(newUserSummary, newUser + " " + user.getUsername() + " !");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}

				HttpSession session = (HttpSession) getFacesContext().getExternalContext().getSession(false);

				session.setAttribute("username", user.getUsername());
				UserDTO userLogged = userService.getUserByUsername(user.getUsername());
				session.setAttribute("user", userLogged);
				session.setAttribute("userId", id);
				userService.logAttempt(user.getUsername(), true);
				isLoggedIn = true;

			return "welcome.xhtml?faces-redirect=true";
		} else {
			UserDTO user1 = userService.getUserByUsername(user.getUsername());
			String summary = bundle.getString("login.error");
			String detail = bundle.getString("login.userPass");

				if(user1 == null){
					getFacesContext().addMessage("loginForm:growl",
							new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
				} else {
					if(user1.isActive() && user1.getPassword() != user.getPassword()) {
						userService.logAttempt(user.getUsername(), false);
						getFacesContext().addMessage("loginForm:growl",
								new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
					} else {
						getFacesContext().addMessage("loginForm:growl",
								new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, "The user is not active!"));
					}
				}
				return "login.xhtml";
			}
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
			return "";
		}
	}

	public String doLogout() {
		try {
			HttpSession session = (HttpSession) getFacesContext().getExternalContext().getSession(false);
			Long id = (Long)session.getAttribute("userId");
			notificationService.deleteNotification(id);
			session.invalidate();
			HttpServletRequest request = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
			HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
			handleLogOutResponse(response, request);
			return "login.xhtml/?faces-redirect=true";
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * Sadly FacesContext is not injectable. For Consistency's sake the
	 * recommended way of getting it is with a producer.
	 *
	 * @return {@link FacesContext}
	 */
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	private void handleLogOutResponse(HttpServletResponse response, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			cookie.setValue(null);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}

}

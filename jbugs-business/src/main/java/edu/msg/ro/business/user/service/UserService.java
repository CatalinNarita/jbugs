package edu.msg.ro.business.user.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import edu.msg.ro.business.exception.JBugsBusinessException;
import edu.msg.ro.business.exception.ObjectNotFoundException;
import edu.msg.ro.business.notification.dto.NotificationDTO;
import edu.msg.ro.business.notification.mapper.NotificationDTOMapper;
import edu.msg.ro.business.notification.service.LinkGenerator;
import edu.msg.ro.business.notification.service.NotificationMessageGenerator;
import edu.msg.ro.business.notification.service.NotificationService;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.dto.mapper.UserDTOMapper;
import edu.msg.ro.persistence.exceptions.JBugsPersistenceException;
import edu.msg.ro.persistence.role.dao.RoleDao;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.role.entity.Role;
import edu.msg.ro.persistence.role.entity.RoleName;
import edu.msg.ro.persistence.user.dao.NotificationDao;
import edu.msg.ro.persistence.user.dao.UserDao;
import edu.msg.ro.persistence.user.entity.LoginHistory;
import edu.msg.ro.persistence.user.entity.Notification;
import edu.msg.ro.persistence.user.entity.User;

@Stateless
public class UserService {

	@EJB
	private UserDao userDao;

	@EJB
	private NotificationDao notificationDao;

	@EJB
	private NotificationMessageGenerator messageGenerator;

	@EJB
	private LinkGenerator linkGenrator;

	@EJB
	private UserDTOMapper userMapper;

	@EJB
	private NotificationDTOMapper notificationMapper;

	@EJB
	private RoleDao roleDao;

	@EJB
	NotificationService notificationService;

	public void saveNewUser(final String firstName, final String lastName, final String phoneNumber, final String email,
			final String password, final List<String> roleNames) throws JBugsBusinessException {
		try {
			final User newUser = new User();
			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);
			newUser.setPhoneNumber(phoneNumber);
			newUser.setEmail(email);
			newUser.setUsername(generateUsername(firstName, lastName));
			newUser.setPassword(password);
			newUser.setActive(true);
			List<Role> roles = new ArrayList<>();
			for (String rn : roleNames) {
				Role role = roleDao.getRole(RoleName.fromDisplayString(rn));
				roles.add(role);
			}
			newUser.setRoles(roles);

			User user = userDao.persistUser(newUser);

			Notification notification = notificationService.generateNewUserNotification(user);
			user.addNotification(notification);

			notificationDao.persistNotification(notification);
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public String generateUsername(String firstName, String lastName) {
		String username;
		final int minUsernameLetters = 6;
		int lastNameLetters;
		if (lastName.length() < 5) {
			username = lastName + firstName;
			lastNameLetters = lastName.length();
		} else {
			username = lastName.substring(0, 5) + firstName.substring(0, 1);
			lastNameLetters = 5;
		}
		if (username.length() > 6)
			username = username.substring(0, 6);
		else if (username.length() < 6) {
			username = username + "0000";
			username = username.substring(0, 6);
		}
		int firstNameLetters = minUsernameLetters - lastName.length();
		if (firstNameLetters < 1)
			firstNameLetters = 1;
		int counter = 1;
		String baseUsername = username;
		while (userDao.getUserByUserName(username) != null)
			if (firstName.length() > firstNameLetters) {
				firstNameLetters++;
				if (lastNameLetters > 1) {
					lastNameLetters--;
					username = username.substring(0, lastNameLetters) + firstName.substring(0, firstNameLetters);
				} else {
					username = firstName.substring(0, 6);
					firstNameLetters = firstName.length();
				}
				baseUsername = new String(username);
			} else {
				username = baseUsername + counter;
				counter++;
			}
		return username;
	}

	public List<UserDTO> getUserByLastName(final String lastName) {
		final List<User> users = userDao.getUserByLastName(lastName);

		return users.stream().map(userEntity -> userMapper.mapToDTO(userEntity)).collect(Collectors.toList());
	}

	/**
	 * Delete the user with the given {@code userId}.
	 *
	 * @param userId
	 *            todo
	 * @return todo
	 * @throws JBugsBusinessException
	 *             if user with given id is not found
	 */
	public boolean deactivateUser(final Long userId) throws JBugsBusinessException {

		try {
			final User user = userDao.findById(userId);
			if (user == null) {
				throw new ObjectNotFoundException("User with id " + userId + " not found!");
			}

			user.setActive(false);

			return true;
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public boolean activateUser(final Long userId) throws JBugsBusinessException {

		try {
			final User user = userDao.findById(userId);
			if (user == null) {
				throw new ObjectNotFoundException("User with id " + userId + " not found!");
			}
			userDao.logAttempt(user.getUsername(), true);
			user.setActive(true);

			return true;
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public void updateUser(UserDTO userDTO) throws JBugsBusinessException {
		User user;
		try {
			user = userDao.findById(userDTO.getId());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEmail(userDTO.getEmail());
			user.setPhoneNumber(userDTO.getPhoneNumber());
			user.setPassword(userDTO.getPassword());
			user.setActive(userDTO.isActive());
			List<Role> roles = new ArrayList<>();
			for (String rn : userDTO.getUserRoles()) {
				String rnString = "";
				if (rn.equals("ADM")) {
					rnString = "administrator";
				}
				if (rn.equals("PM")) {
					rnString = "project manager";
				}
				if (rn.equals("TM")) {
					rnString = "test manager";
				}
				if (rn.equals("DEV")) {
					rnString = "developer";
				}
				if (rn.equals("TEST")) {
					rnString = "tester";
				}
				if (rn.equals(null)) {
					rnString = "";
				}
				Role role = roleDao.getRole(RoleName.fromDisplayString(rnString));
				roles.add(role);
			}
			user.setRoles(roles);
		} catch (JBugsPersistenceException e1) {
			throw new JBugsBusinessException(e1.getMessage(), e1);
		}
		try {
			userDao.persistUser(user);
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public List<UserDTO> getAllUsers() {
		final List<User> allUsers = userDao.getAll();
		return allUsers.stream().map(userEntity -> userMapper.mapToDTO(userEntity)).collect(Collectors.toList());
	}

	public UserDTO getUserByUsername(String userName) {
		User user = userDao.getUserByUserName(userName);
		if (user != null) {
			return userMapper.mapToDTO(user);
		} else
			return null;
	}

	public boolean isValidUser(UserDTO loginUser) {
		UserDTO savedUser = getUserByUsername(loginUser.getUsername());
		if (savedUser != null && hasFiveFails(savedUser.getUsername())) {
			try {
				FacesContext context = FacesContext.getCurrentInstance();
				ResourceBundle bundle = ResourceBundle.getBundle("jbugs.messages", context.getViewRoot().getLocale());
				String summary = bundle.getString("login.inactiveSummary");
				String detail = MessageFormat.format(bundle.getString("login.inactiveDetail"), savedUser.getUsername());
				deactivateUser(savedUser.getId());
				FacesContext.getCurrentInstance().addMessage("loginForm:username", new FacesMessage(summary, detail));
			} catch (JBugsBusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (savedUser != null && savedUser.getPassword() != null && savedUser.isActive()) {
			return savedUser.getPassword().equals(loginUser.getPassword());
		}
		return false;
	}

	public List<Long> hasTasks(Long id) {
		return userDao.hasTasks(id);
	}

	public void logAttempt(String username, Boolean result) throws JBugsBusinessException {
		try {
			userDao.logAttempt(username, result);
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public boolean hasFiveFails(String username) {
		int fails = 1;
		List<LoginHistory> history = userDao.findLastFiveAttemps(username);
		for (LoginHistory h : history) {
			if (h.getSuccess() == false) {
				fails++;
			}
		}
		return fails == 5;

	}

	public boolean checkIfNewUser(String username) {
		return userDao.checkIfNewUser(username);
	}

	public boolean hasRole(String username, RoleName name) {
		return userDao.getUserRoles(username).stream().map(r -> r.getRoleName()).collect(Collectors.toList())
				.contains(name);
	}

	public boolean hasPermission(String username, PermissionType type) {
		List<PermissionType> permissionTypes = new ArrayList<>();
		userDao.getUserRoles(username).forEach(r -> {
			r.getPermissions().forEach(p -> permissionTypes.add(p.getPermissionName()));
		});
		return permissionTypes.contains(type);
	}

	public boolean checkModified(final UserDTO newUser) throws JBugsBusinessException {
		try {
			User oldUser = userDao.findById(newUser.getId());
			return !newUser.getEmail().equals(oldUser.getEmail())
					|| !newUser.getFirstName().equals(oldUser.getFirstName())
					|| !newUser.getLastName().equals(oldUser.getLastName())
					|| !newUser.getPassword().equals(oldUser.getPassword())
					|| !newUser.getPhoneNumber().equals(oldUser.getPhoneNumber())
					|| !newUser.getUserRoles().equals(oldUser.getRoles());
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public List<String> getAllRolesAsString() {
		final List<Role> rolesDTO = roleDao.findAllRoles();
		final List<String> roles = new ArrayList<>();
		for (int i = 0; i < rolesDTO.size(); i++) {
			roles.add(rolesDTO.get(i).getRoleName().toString());
		}
		return roles;
	}

	public void deleteHistory(Long id) throws JBugsBusinessException {
		try {
			userDao.deleteHistory(id);
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public boolean checkIfEmailExists(String email) {
		return userDao.checkIfEmailExists(email);
	}

	public List<NotificationDTO> getNotifications(Long userId) {
		List<Notification> notifications = userDao.getUserNotifications(userId);
		List<NotificationDTO> notificationDTOs = new ArrayList<>();
		notifications.forEach(n -> notificationDTOs.add(notificationMapper.mapToDTO(n)));
		Collections.sort(notificationDTOs,
				(a, b) -> (int) (b.getCreationDate().getTime() - a.getCreationDate().getTime()));
		return notificationDTOs;
	}

	public User findById(Long id) throws JBugsBusinessException {
		try {
			return userDao.findById(id);
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public List<User> getUsersByRole(RoleName roleName) {
		return userDao.getUsersByRole(roleName);
	}

}

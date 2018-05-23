package edu.msg.ro.persistence.user.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import edu.msg.ro.persistence.exceptions.JBugsPersistenceException;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.role.entity.Role;
import edu.msg.ro.persistence.role.entity.RoleName;
import edu.msg.ro.persistence.user.entity.LoginHistory;
import edu.msg.ro.persistence.user.entity.Notification;
import edu.msg.ro.persistence.user.entity.NotificationType;
import edu.msg.ro.persistence.user.entity.User;

/**
 * TODO: add javadoc create AbtractDao (findById generic)
 * 
 * @author Andrei Floricel, msg systems ag
 *
 */
@Stateless
public class UserDao {

	@PersistenceContext(unitName = "jbugs-persistence")
	private EntityManager em;

	private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

	public User persistUser(final User user) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "saving user with id " + user.getId() + "and username " + user.getUsername());
			em.persist(user);
			em.flush();
			LOGGER.log(Level.ALL,
					"saving user with id " + user.getId() + "and username " + user.getUsername() + " was successful");
			return user;
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public List<User> getUserByLastName(final String lastName) {
		final TypedQuery<User> query = em.createNamedQuery(User.FIND_USER_BY_LASTNAME, User.class);
		query.setParameter("lastName", lastName);

		return query.getResultList();
	}

	public User getUserByUserName(final String userName) {
		final TypedQuery<User> query = em.createNamedQuery(User.FIND_USER_BY_USERNAME, User.class);
		query.setParameter("username", userName);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public User findById(final Long id) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "finding user with id " + id);
			return this.em.find(User.class, id);
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public List<User> getAll() {
		final TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL_USERS, User.class);
		return query.getResultList();
	}

	public void logAttempt(String username, Boolean result) throws JBugsPersistenceException {
		try {
			LoginHistory loginHistory = new LoginHistory();
			loginHistory.setAttemptTime(new Timestamp(System.currentTimeMillis()));
			loginHistory.setUsername(username);
			loginHistory.setSuccess(result);
			LOGGER.log(Level.ALL, "saving login history for user " + username);
			em.persist(loginHistory);
			LOGGER.log(Level.ALL, "saving login history for user " + username + " was successful");
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public List<LoginHistory> findLastFiveAttemps(String username) {
		final TypedQuery<LoginHistory> query = em.createNamedQuery(LoginHistory.FIND_LAST_FIVE_ATTEMPS,
				LoginHistory.class);
		query.setParameter("username", username);
		query.setMaxResults(5);
		return query.getResultList();
	}

	public List<Long> hasTasks(Long id) {
		final TypedQuery<Long> query = em.createNamedQuery(User.HAS_TASKS, Long.class);
		query.setParameter("idUser", id);
		return query.getResultList();
	}

	public boolean checkIfNewUser(String username) {

		Long id = getUserByUserName(username).getId();

		final TypedQuery<Notification> query = em.createNamedQuery(User.CHECK_IF_NEW_USER, Notification.class);
		query.setParameter("idUser", id);
		query.setParameter("notificationType", NotificationType.WELCOME_NEW_USER);
		try {
			query.getSingleResult();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public List<Role> getUserRoles(String username) {
		User user = getUserByUserName(username);
		return user.getRoles();
	}

	public List<Notification> getUserNotifications(Long userId) {
		final TypedQuery<Notification> query = em.createNamedQuery(Notification.FIND_NOTIFICATION_BY_USERID,
				Notification.class);
		query.setParameter("userId", userId);
		return query.getResultList();

	}

	public User addNotification(String username, Notification notification) {
		User user = getUserByUserName(username);
		user.addNotification(notification);
		return user;
	}

	public User addNotifications(String username, List<Notification> notification) {
		User user = getUserByUserName(username);
		notification.forEach(n -> user.addNotification(n));
		return user;
	}

	public List<LoginHistory> getFailedAttempts(Long id) throws JBugsPersistenceException {
		try {
			String username = findById(id).getUsername();
			final TypedQuery<LoginHistory> query = em.createNamedQuery(LoginHistory.GET_FAILED_ATTEMPTS,
					LoginHistory.class);
			query.setParameter("username", username);
			return query.getResultList();
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public void deleteHistory(Long id) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "deleting histoy for user with id " + id);
			List<LoginHistory> failedAttempts = getFailedAttempts(id);
			failedAttempts.forEach(attempt -> em.remove(attempt));
			LOGGER.log(Level.ALL, "deleting histoy for user with id " + id + " was successful");
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public boolean checkIfEmailExists(String email) {
		final TypedQuery<User> query = em.createNamedQuery(User.CHECK_IF_EMAIL_EXISTS, User.class);
		query.setParameter("email", email);
		try {
			query.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<User> getUsersWithUserManagementRight() {

		final TypedQuery<User> query = em.createQuery(
				"select u from User u join fetch u.roles r join fetch r.permissions p where p.permissionName = :permissionName",
				User.class);
		query.setParameter("permissionName", PermissionType.USER_MANAGEMENT);
		return query.getResultList();
	}

	public List<User> getUsersByRole(RoleName roleName) {
		final TypedQuery<User> query = em.createNamedQuery(User.GET_USERS_BY_ROLE, User.class);
		query.setParameter("role", roleName);
		return query.getResultList();
	}

}

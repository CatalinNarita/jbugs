package edu.msg.ro.business.notification.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import edu.msg.ro.business.bug.dto.BugDTO;
import edu.msg.ro.business.bug.dto.mapper.BugDTOMapper;
import edu.msg.ro.business.exception.JBugsBusinessException;
import edu.msg.ro.business.notification.dto.NotificationDTO;
import edu.msg.ro.business.notification.mapper.NotificationDTOMapper;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.dto.mapper.UserDTOMapper;
import edu.msg.ro.persistence.bug.dao.BugDao;
import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.exceptions.JBugsPersistenceException;
import edu.msg.ro.persistence.user.dao.NotificationDao;
import edu.msg.ro.persistence.user.dao.UserDao;
import edu.msg.ro.persistence.user.entity.Notification;
import edu.msg.ro.persistence.user.entity.NotificationType;
import edu.msg.ro.persistence.user.entity.User;

/**
 *
 * TODO: make CDI Interceptor/decorator?
 *
 */
@Stateless
public class NotificationService {

	@EJB
	private NotificationDao notificationDao;

	@EJB
	private NotificationDTOMapper notificationMapper;

	@EJB
	private LinkGenerator linkGenerator;

	@EJB
	private NotificationMessageGenerator messageGenerator;

	@EJB
	private UserDao userDao;

	@EJB
	private UserDTOMapper userDTOMapper;

	@EJB
	private BugDTOMapper bugDTOMapper;

	@EJB
	private BugDao bugDao;

	public List<NotificationDTO> getAllNotifications() {
		final List<Notification> allNotifications = notificationDao.getAll();
		return allNotifications.stream().map(notificationEntity -> notificationMapper.mapToDTO(notificationEntity))
				.collect(Collectors.toList());
	}

	public void saveNewNotification(final String notificationType, final Date creationDate, final User destinationUser,
			final Bug bug) throws JBugsBusinessException {
		try {
			final Notification notification = new Notification();
			notification.setNotificationType(NotificationType.fromDisplayString(notificationType.toLowerCase()));
			notification.setCreationDate(creationDate);
			notification.setDestinationUser(destinationUser);
			notification.setBug(bug);
			notification.setLink(linkGenerator.getLink(notification.getBug()));

			notificationDao.persistNotification(notification);
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}

	}

	public Notification generateNewUserNotification(final User user) {
		final Notification notification = new Notification();
		UserDTO userDTO = userDTOMapper.mapToDTO(user);
		notification.setNotificationType(NotificationType.WELCOME_NEW_USER);
		notification.setCreationDate(new Date());
		notification.setDestinationUser(user);
		notification.setMessage(messageGenerator.generate(userDTO, NotificationType.WELCOME_NEW_USER));
		return notification;
	}

	public Notification saveBugCreatedNotification(final Bug bug) {

		User user = bug.getCreatedBy();

		Notification notification = new Notification();
		notification.setNotificationType(NotificationType.BUG_UPDATED);
		notification.setCreationDate(new Date());
		notification.setLink(linkGenerator.getLink(bug));
		notification.setDestinationUser(user);
		notification.setMessage(messageGenerator.generate(bugDTOMapper.mapToDTO(bug), NotificationType.BUG_UPDATED));
		notification.setBug(bug);
		userDao.getUserByUserName(user.getUsername()).addNotification(notification);

		user = bug.getAssignedTo();

		notification = new Notification();
		notification.setNotificationType(NotificationType.BUG_UPDATED);
		notification.setCreationDate(new Date());
		notification.setLink(linkGenerator.getLink(bug));
		notification.setDestinationUser(user);
		notification.setMessage(messageGenerator.generate(bugDTOMapper.mapToDTO(bug), NotificationType.BUG_UPDATED));
		notification.setBug(bug);
		userDao.getUserByUserName(user.getUsername()).addNotification(notification);

		return notification;
	}

	public Notification saveBugUpdatedNotification(BugDTO bugDTO) throws JBugsBusinessException {
		try {
			Bug bug = bugDao.findById(bugDTO.getId());

			User user = bugDTO.getCreatedBy();

			Notification notification = new Notification();
			notification.setNotificationType(NotificationType.BUG_UPDATED);
			notification.setCreationDate(new Date());
			notification.setLink(linkGenerator.getLink(bug));
			notification.setDestinationUser(user);
			notification.setMessage(
					messageGenerator.generate(bugDTOMapper.mapToDTO(bugDao.findById(bugDTO.getId())), bugDTO));
			notification.setBug(bugDao.findById(bugDTO.getId()));
			userDao.getUserByUserName(user.getUsername()).addNotification(notification);
			user = bugDTO.getAssignedTo();

			notification = new Notification();
			notification.setNotificationType(NotificationType.BUG_UPDATED);
			notification.setCreationDate(new Date());
			notification.setLink(linkGenerator.getLink(bug));
			notification.setDestinationUser(user);
			notification.setMessage(
					messageGenerator.generate(bugDTOMapper.mapToDTO(bugDao.findById(bugDTO.getId())), bugDTO));
			notification.setBug(bugDao.findById(bugDTO.getId()));
			userDao.getUserByUserName(user.getUsername()).addNotification(notification);

			return notification;
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public Notification saveBugCloseNotification(BugDTO bugDTO) throws JBugsBusinessException {
		try {
			Bug bug = bugDao.findById(bugDTO.getId());

			User user = bugDTO.getCreatedBy();

			Notification notification = new Notification();
			notification.setNotificationType(NotificationType.BUG_CLOSED);
			notification.setCreationDate(new Date());
			notification.setLink(linkGenerator.getLink(bug));
			notification.setDestinationUser(user);
			notification.setMessage(
					messageGenerator.generate(bugDTOMapper.mapToDTO(bugDao.findById(bugDTO.getId())), bugDTO));
			notification.setBug(bugDao.findById(bugDTO.getId()));
			userDao.getUserByUserName(user.getUsername()).addNotification(notification);

			user = bugDTO.getAssignedTo();

			notification = new Notification();
			notification.setNotificationType(NotificationType.BUG_CLOSED);
			notification.setCreationDate(new Date());
			notification.setLink(linkGenerator.getLink(bug));
			notification.setDestinationUser(user);
			notification.setMessage(
					messageGenerator.generate(bugDTOMapper.mapToDTO(bugDao.findById(bugDTO.getId())), bugDTO));
			notification.setBug(bugDao.findById(bugDTO.getId()));
			userDao.getUserByUserName(user.getUsername()).addNotification(notification);

			return notification;
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public Notification saveBugStatusNotification(BugDTO bugDTO, BugStatusType status) throws JBugsBusinessException {
		try {
			Bug bug = bugDao.findById(bugDTO.getId());

			User user = bugDTO.getCreatedBy();

			Notification notification = new Notification();
			notification.setNotificationType(NotificationType.BUG_STATUS_UPDATED);
			notification.setCreationDate(new Date());
			notification.setLink(linkGenerator.getLink(bug));
			notification.setDestinationUser(user);
			notification.setMessage(messageGenerator.generate(bugDTO, status));
			notification.setBug(bugDao.findById(bugDTO.getId()));
			userDao.getUserByUserName(user.getUsername()).addNotification(notification);

			user = bugDTO.getAssignedTo();

			notification = new Notification();
			notification.setNotificationType(NotificationType.BUG_STATUS_UPDATED);
			notification.setCreationDate(new Date());
			notification.setLink(linkGenerator.getLink(bug));
			notification.setDestinationUser(user);
			notification.setMessage(messageGenerator.generate(bugDTO, status));
			notification.setBug(bugDao.findById(bugDTO.getId()));
			userDao.getUserByUserName(user.getUsername()).addNotification(notification);

			return notification;
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public void deleteNotification(Long id) throws JBugsBusinessException {
		try {
			notificationDao.deleteNotification(id);
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public void deleteOutdatedNotifications() {
		notificationDao.deleteOutdatedNotifications();
	}

	public List<Notification> getOutdatedNotifications() {
		return notificationDao.getOutdadedNotifications();
	}

	public Notification addUserNotification(NotificationType notificationType, UserDTO user, Long adminId)
			throws JBugsBusinessException {
		try {
			Notification notification = new Notification();
			User oldUser = userDao.findById(user.getId());
			User admin = userDao.findById(adminId);

			notification.setNotificationType(notificationType);
			notification.setCreationDate(new Date());
			notification.setDestinationUser(oldUser);
			notification.setMessage(messageGenerator.generate(userDTOMapper.mapToDTO(oldUser), user));

			notificationDao.persistNotification(notification);

			notification = new Notification();

			notification.setNotificationType(notificationType);
			notification.setCreationDate(new Date());
			notification.setDestinationUser(admin);
			notification.setMessage(messageGenerator.generate(userDTOMapper.mapToDTO(oldUser), user));

			notificationDao.persistNotification(notification);

			return notification;
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

	public Notification addUserStatusNotification(Long id, NotificationType type, List<User> admins)
			throws JBugsBusinessException {
		try {
			User initial = userDao.findById(id);
			Notification notification = new Notification();
			for (User u : admins) {
				notification = new Notification();
				notification.setNotificationType(type);
				notification.setCreationDate(new Date());
				notification.setLink(linkGenerator.getLink(initial));
				notification.setMessage(messageGenerator.generate(userDTOMapper.mapToDTO(initial), type));
				notification.setDestinationUser(u);
				u.addNotification(notification);
				notificationDao.persistNotification(notification);
			}
			return notification;
		} catch (JBugsPersistenceException e) {
			throw new JBugsBusinessException(e.getMessage(), e);
		}
	}

}

package edu.msg.ro.persistence.user.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import edu.msg.ro.persistence.exceptions.JBugsPersistenceException;
import edu.msg.ro.persistence.user.entity.Notification;

@Stateless
public class NotificationDao {
	
	@PersistenceContext(unitName = "jbugs-persistence")
	private EntityManager em;
	
	private static final Logger LOGGER = Logger.getLogger( NotificationDao.class.getName() );
	
	public void persistNotification(Notification notification) throws JBugsPersistenceException{
		try {
			LOGGER.log(Level.ALL, "saving notification with id " + notification.getId());
			em.persist(notification);
			LOGGER.log(Level.ALL, "saving notification with id " + notification.getId()+" was successful");
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(),e);
		}
	}
	
	public Notification findById(final Long id) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "finding notification with id " + id);
			return this.em.find(Notification.class, id);
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(),e);
		}
	}
	
	public List<Notification> getAll() {
		final TypedQuery<Notification> query = em.createNamedQuery(Notification.FIND_ALL_NOTIFICATIONS, Notification.class);
		return query.getResultList();
	}
	
	public Notification getNotificationByUserId(Long id) {
		final TypedQuery<Notification> query = em.createNamedQuery(Notification.FIND_NOTIFICATION_BY_USERID, Notification.class);
		query.setParameter("userId", id);
		try{
			return query.getSingleResult();
		} catch (Exception e){
			return null;
		}
	}
	
	public void deleteNotification(Long id) throws JBugsPersistenceException{
		try {
			LOGGER.log(Level.ALL, "deleting notification with id " + id);
			Notification notification = getNotificationByUserId(id);
			if(notification != null){
				em.remove(notification);
			}
			LOGGER.log(Level.ALL, "deleting notification with id " + id+" was successful");
		}catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(),e);
		}
	}
	
	public List<Notification> getOutdadedNotifications(){
		List<Notification> notifications = getAll();
		List<Notification> outdatedNotifications = new ArrayList<>();
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		
		for(Notification notification : notifications){
			calendar.setTime(notification.getCreationDate());
			calendar.add(Calendar.DATE, 30);
			if(date.after(calendar.getTime())){
				outdatedNotifications.add(notification);
			}
		}

		return outdatedNotifications;
	}
	
	public void deleteOutdatedNotifications(){
		List<Notification> notifications = getOutdadedNotifications();
		notifications.stream()
		.forEach(notification -> em.remove(notification));
	}

}

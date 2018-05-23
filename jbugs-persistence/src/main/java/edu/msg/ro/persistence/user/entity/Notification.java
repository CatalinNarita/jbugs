package edu.msg.ro.persistence.user.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import edu.msg.ro.persistence.bug.entity.Bug;

@NamedQueries({ @NamedQuery(name = Notification.FIND_ALL_NOTIFICATIONS, query = "SELECT n from Notification n"),
		@NamedQuery(name = Notification.FIND_NOTIFICATION_BY_USERID, query = "SELECT n from Notification n where n.destinationUser.id = :userId"),
		@NamedQuery(name = Notification.GET_OUTDATED_NOTIFICATIONS, query = "SELECT n FROM Notification n WHERE CURRENT_DATE > n.creationDate + :amount") })
@Entity
public class Notification extends AbstractEntity {

	public static final String FIND_ALL_NOTIFICATIONS = "Notification.findAllNotifications";
	public static final String FIND_NOTIFICATION_BY_USERID = "Notification.findNotificationByUserId";
	public static final String GET_OUTDATED_NOTIFICATIONS = "Notification.getOutdatedNotifications";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	@Column
	private Date creationDate;

	@Column(length = 1500)
	private String message;

	@Column
	private String link;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private User destinationUser;

	@OneToOne
	@JoinColumn(name = "bugId")
	private Bug bug;

	@Override
	public Long getId() {
		return this.id;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(final NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getDestinationUser() {
		return destinationUser;
	}

	public void setDestinationUser(User destinationUser) {
		this.destinationUser = destinationUser;
	}

	public Bug getBug() {
		return bug;
	}

	public void setBug(Bug bug) {
		this.bug = bug;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}

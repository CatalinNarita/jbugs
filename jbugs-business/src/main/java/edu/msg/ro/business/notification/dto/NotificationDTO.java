package edu.msg.ro.business.notification.dto;

import java.util.Date;

import edu.msg.ro.business.dto.AbstractDTO;
import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.user.entity.NotificationType;
import edu.msg.ro.persistence.user.entity.User;

public class NotificationDTO extends AbstractDTO{
	
	private NotificationType notificationType;

	private Date creationDate;

	private User destinationUser;
	
	private Bug bug;
	
	private String link;
	
	private String message;

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bug == null) ? 0 : bug.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((destinationUser == null) ? 0 : destinationUser.hashCode());
		result = prime * result + ((notificationType == null) ? 0 : notificationType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationDTO other = (NotificationDTO) obj;
		if (bug == null) {
			if (other.bug != null)
				return false;
		} else if (!bug.equals(other.bug))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (destinationUser == null) {
			if (other.destinationUser != null)
				return false;
		} else if (!destinationUser.equals(other.destinationUser))
			return false;
		if (notificationType != other.notificationType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotificationDTO [notificationType=" + notificationType + ", creationDate=" + creationDate
				+ ", destinationUser=" + destinationUser + ", bug=" + bug + "]";
	}
	
	
	
	

}

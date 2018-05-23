package edu.msg.ro.business.notification.mapper;

import javax.ejb.Stateless;

import edu.msg.ro.business.notification.dto.NotificationDTO;
import edu.msg.ro.persistence.user.entity.Notification;

@Stateless
public class NotificationDTOMapper {

	public NotificationDTO mapToDTO(final Notification notificationEntity) {
		final NotificationDTO notificationDTO = new NotificationDTO();
		
		notificationDTO.setId(notificationEntity.getId());
		notificationDTO.setDestinationUser(notificationEntity.getDestinationUser());
		notificationDTO.setBug(notificationEntity.getBug());
		notificationDTO.setCreationDate(notificationEntity.getCreationDate());
		notificationDTO.setNotificationType(notificationEntity.getNotificationType());
		notificationDTO.setLink(notificationEntity.getLink());
		notificationDTO.setMessage(notificationEntity.getMessage());
		
		return notificationDTO;
	}

	public Notification mapToEntity(final NotificationDTO notificationDTO) {
		final Notification notificationEntity = new Notification();
		notificationEntity.setDestinationUser(notificationDTO.getDestinationUser());
		notificationEntity.setBug(notificationDTO.getBug());
		notificationEntity.setCreationDate(notificationDTO.getCreationDate());
		notificationEntity.setNotificationType(notificationDTO.getNotificationType());

		return notificationEntity;
	}
	
}

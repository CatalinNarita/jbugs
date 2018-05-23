package edu.msg.ro.persistence.user.entity;

public enum NotificationType {

	/**
	 * Sent by user deletion
	 */
	WELCOME_NEW_USER("welcome"),

	USER_UPDATED("user updated"),

	BUG_UPDATED("bug updated"),

	BUG_CLOSED("bug closed"),

	BUG_STATUS_UPDATED("bug status updated"),

	USER_DEACTIVATED("user deactivated"),
	USER_ACTIVATED("user activated");

	String type;

	private NotificationType(String type) {
		this.type = type;
	}

	public static NotificationType fromDisplayString(String displayString) {
		for (NotificationType type : NotificationType.values())
			if (type.getType().equals(displayString))
				return type;
		return null;
	}

	public String getType() {
		return type;
	}

}

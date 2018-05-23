package edu.msg.ro.business.notification.service;

import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import edu.msg.ro.business.bug.dto.BugDTO;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.user.entity.NotificationType;

@Stateless
public class NotificationMessageGenerator {
	@EJB
	private LanguageLocale languageLocale;

	private ResourceBundle messages;

	Function<UserDTO, String> welcome = (u) -> messages.getString("notifications.messages.welcome") + ": "
			+ u.getFirstName() + "!\n" + details(u);

	Function<UserDTO, String> delete = (u) -> messages.getString("notifications.messages.deletedUser") + ": "
			+ u.getUsername() + "!\n" + details(u);

	BiFunction<UserDTO, UserDTO, String> userUpdate = (u1, u2) -> getChangedDetails(u1, u2);

	Function<UserDTO, String> deactivated = (u) -> messages.getString("notifications.messages.deactivatedUser") + ": "
			+ u.getUsername() + "!\n" + details(u);

	Function<UserDTO, String> activated = (u) -> messages.getString("notifications.messages.activatedUser") + ": "
			+ u.getUsername() + "!\n" + details(u);

	BiFunction<BugDTO, BugDTO, String> bugUpdated = (b1, b2) -> getChangedDetails(b1, b2);

	Function<BugDTO, String> bugCreated = (b) -> messages.getString("notifications.messages.bugCreated") + ": "
			+ details(b);

	Function<BugDTO, String> bugClosed = (b) -> messages.getString("notifications.messages.bugClosed") + ": "
			+ details(b);

	BiFunction<BugDTO, BugStatusType, String> bugStatusUpdated = (b,
			s) -> messages.getString("notifications.messages.bugStatusUpdate") + b.getTitle() + "\n" + s + " -> "
					+ b.getStatus() + "\n" + details(b);

	BiFunction<String, String, String> changed = (a, b) -> {
		if (a == null) {
			return " -> " + b;
		}
		if (!a.equals(b)) {
			return a + " -> " + b;
		}
		;
		return null;
	};

	BiFunction<String, String, String> getMessage = (a, b) -> {
		if (b == null) {
			return "";
		}
		return a + ": " + b + "\n";
	};

	private String details(BugDTO b) {
		return "Title: " + b.getTitle() + "\n" + "Description: " + b.getDescription() + "\n" + "Version: "
				+ b.getVersion() + "\n" + "Severity: " + b.getSeverity() + "\n" + "Status: " + b.getStatus() + "\n"
				+ "Created by:" + b.getCreatedBy().getUsername() + "\n" + "Assigned to: "
				+ b.getAssignedTo().getUsername() + "\n";
	}

	private String details(UserDTO u) {
		return "Details:\n" + "Username: " + u.getUsername() + "\n" + "First Name: " + u.getFirstName() + "\n"
				+ "Last Name: " + u.getLastName() + "\n" + "Email: " + u.getEmail() + "\n" + "Phone: "
				+ u.getPhoneNumber() + "\n";
	}

	private String getChangedDetails(UserDTO start, UserDTO end) {
		StringBuilder builder = new StringBuilder();
		builder.append(start.getUsername() + "\n");

		builder.append(getMessage.apply("First Name", changed.apply(start.getFirstName(), end.getFirstName())));

		builder.append(getMessage.apply("Last Name", changed.apply(start.getLastName(), end.getLastName())));

		builder.append(getMessage.apply("Email", changed.apply(start.getEmail(), end.getEmail())));

		builder.append(getMessage.apply("Phone", changed.apply(start.getPhoneNumber(), end.getPhoneNumber())));

		return builder.toString();
	}

	private String getChangedDetails(BugDTO start, BugDTO end) {
		StringBuilder builder = new StringBuilder();
		builder.append("Bug number: " + start.getId() + "\n");

		builder.append(getMessage.apply("Title", changed.apply(start.getTitle(), end.getTitle())));

		builder.append(getMessage.apply("Description", changed.apply(start.getDescription(), end.getDescription())));

		builder.append(getMessage.apply("Status", changed.apply(start.getStatus(), end.getStatus())));

		builder.append(getMessage.apply("Severity", changed.apply(start.getSeverity(), end.getSeverity())));

		builder.append(getMessage.apply("Version", changed.apply(start.getVersion(), end.getVersion())));

		builder.append(getMessage.apply("Fixed in version",
				changed.apply(start.getFixedInVersion(), end.getFixedInVersion())));

		builder.append(getMessage.apply("Target Date",
				changed.apply(start.getTargetDate().toString(), end.getTargetDate().toString())));

		return builder.toString();
	}

	public String generate(UserDTO user, NotificationType type) {
		updateResourceBundle();
		if (type.equals(NotificationType.WELCOME_NEW_USER)) {
			return welcome.apply(user);
		} else if (type.equals(NotificationType.USER_DEACTIVATED)) {
			return deactivated.apply(user);
		} else if (type.equals(NotificationType.USER_ACTIVATED))
			return activated.apply(user);
		return delete.apply(user);
	}

	public String generate(UserDTO oldUser, UserDTO newUser) {
		updateResourceBundle();
		return userUpdate.apply(oldUser, newUser);
	}

	public String generate(BugDTO oldBug, BugDTO newBug) {
		updateResourceBundle();
		return "Bug was updated:\n" + bugUpdated.apply(oldBug, newBug);
	}

	public String generate(BugDTO bug, NotificationType type) {
		updateResourceBundle();
		if (type.equals(NotificationType.BUG_UPDATED)) {
			return bugCreated.apply(bug);
		}
		return bugClosed.apply(bug);
	}

	public String generate(BugDTO bug, BugStatusType type) {
		updateResourceBundle();
		return bugStatusUpdated.apply(bug, type);
	}

	private void updateResourceBundle() {
		messages = ResourceBundle.getBundle("jbugs.messages", languageLocale.getLocale());
	}

}

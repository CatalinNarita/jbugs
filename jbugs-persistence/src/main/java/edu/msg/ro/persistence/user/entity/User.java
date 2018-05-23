package edu.msg.ro.persistence.user.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.role.entity.Role;


@NamedQueries({
		@NamedQuery(name = User.FIND_USER_BY_LASTNAME, query = "SELECT u from User u WHERE u.lastName = :lastName"),
		@NamedQuery(name = User.FIND_USER_BY_USERNAME, query = "SELECT u from User u WHERE u.username = :username"),
		@NamedQuery(name = User.FIND_ALL_USERS, query = "SELECT u from User u"),
		@NamedQuery(name = User.HAS_TASKS, query = "select u.id from User u, Bug b where u.id = b.assignedTo.id and u.id = :idUser"),
		@NamedQuery(name = User.CHECK_IF_NEW_USER, query = "select n from Notification n, User u where u.id = n.destinationUser.id and u.id = :idUser and n.notificationType = :notificationType"),
		@NamedQuery(name = User.CHECK_IF_EMAIL_EXISTS, query = "select u from User u where u.email = :email"),
		@NamedQuery(name = User.GET_USERS_BY_ROLE, query = "select u from User u join fetch u.roles r where r.roleName = :role")
		})

@Entity
public class User extends AbstractEntity {

	public static final String FIND_USER_BY_LASTNAME = "User.findUserByLastName";
	public static final String FIND_USER_BY_USERNAME = "User.findUserByUserName";
	public static final String FIND_ALL_USERS = "User.findAllUsers";
	public static final String HAS_TASKS ="User.hasTasks";
	public static final String CHECK_IF_NEW_USER ="User.newUser";
	public static final String CHECK_IF_EMAIL_EXISTS ="User.emailExists";
	public static final String GET_USERS_BY_ROLE = "User.getUsersByRole";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Column(length = 20)
	private String phoneNumber;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((bugsAssigned == null) ? 0 : bugsAssigned.hashCode());
		result = prime * result + ((bugsCreated == null) ? 0 : bugsCreated.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((notifications == null) ? 0 : notifications.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (active != other.active)
			return false;
		if (bugsAssigned == null) {
			if (other.bugsAssigned != null)
				return false;
		} else if (!bugsAssigned.equals(other.bugsAssigned))
			return false;
		if (bugsCreated == null) {
			if (other.bugsCreated != null)
				return false;
		} else if (!bugsCreated.equals(other.bugsCreated))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (notifications == null) {
			if (other.notifications != null)
				return false;
		} else if (!notifications.equals(other.notifications))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Column(unique = true, length = 50)
	private String email;

	@Column(unique = true, length = 15)
	private String username;

	@Column(length = 32)
	private String password;

	@Column(name = "Status")
	private boolean active;

	@ManyToMany
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
	private List<Role> roles = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy", cascade = CascadeType.ALL)
	private List<Bug> bugsCreated = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedTo", cascade = CascadeType.ALL)
	private List<Bug> bugsAssigned = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "destinationUser", cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private List<Comment> comments  = new ArrayList<>();

	public User addNotification(Notification notification){
		this.notifications.add(notification);
		return this;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public List<Bug> getBugsAssigned() {
		return bugsAssigned;
	}

	public void setBugsAssigned(List<Bug> bugsAssigned) {
		this.bugsAssigned = bugsAssigned;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public List<Bug> getBugsCreated() {
		return bugsCreated;
	}

	public void setBugsCreated(List<Bug> bugsCreated) {
		this.bugsCreated = bugsCreated;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean activ) {
		this.active = activ;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(final List<Role> roles) {
		this.roles = roles;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}

}
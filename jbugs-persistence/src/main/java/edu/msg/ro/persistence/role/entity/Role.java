package edu.msg.ro.persistence.role.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import edu.msg.ro.persistence.user.entity.User;

@NamedQueries({ @NamedQuery(name = Role.FIND_ROLE, query = "SELECT r from Role r WHERE r.roleName = :roleName"),
		@NamedQuery(name = Role.FIND_ALL_ROLES, query = "SELECT r from Role r") })
@Entity
public class Role {
	public static final String FIND_ROLE = "Role.findRole";
	public static final String FIND_ALL_ROLES = "Role.findAllRoles";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private RoleName roleName;

	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "ROLE_PERMISSION", joinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_permission", referencedColumnName = "id"))
	private List<Permission> permissions = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(final RoleName roleName) {
		this.roleName = roleName;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(final List<User> users) {
		this.users = users;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(final List<Permission> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(Permission permission) {
		if (!permissions.contains(permission)) {
			permissions.add(permission);
		}

	}

	public void addPermissions(List<Permission> permissions) {
		for (Permission p : permissions) {
			addPermission(p);
		}
	}

	public void removePermission(Permission permission) {
		permissions.remove(permission);
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		if (roleName != other.roleName)
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", roleName=" + roleName + ", users=" + users + ", permissions=" + permissions + "]";
	}
	
	
}
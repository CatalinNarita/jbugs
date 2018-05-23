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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
		@NamedQuery(name = Permission.FIND_PERMISSION, query = "SELECT p from Permission p WHERE p.permissionName = :permissionName"),
		@NamedQuery(name = Permission.FIND_ALL_PERMISSIONS, query = "SELECT p from Permission p") })

@Entity
public class Permission {
	public static final String FIND_PERMISSION = "Permission.findPermission";
	public static final String FIND_ALL_PERMISSIONS = "Permission.findAllPermissions";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	@Enumerated(EnumType.STRING)
	private PermissionType permissionName;

	private String description;

	@ManyToMany(mappedBy = "permissions")
	private List<Role> roles = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public PermissionType getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(final PermissionType permissionName) {
		this.permissionName = permissionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Permission [id=" + id + ", permissionName=" + permissionName + ", description=" + description
				+ ", roles=" + roles + "]";
	}
	
	
}
package edu.msg.ro.persistence.role.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import edu.msg.ro.persistence.role.entity.Permission;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.role.entity.Role;
import edu.msg.ro.persistence.role.entity.RoleName;

@Stateless
public class RoleDao {
	@PersistenceContext(unitName = "jbugs-persistence")
	private EntityManager em;

	public Role addPermission(RoleName roleName, PermissionType permissionType) {
		Role role = getRole(roleName);

		Permission permission = getPermission(permissionType);

		role.addPermission(permission);

		return role;
	}

	public List<Permission> addPermissions(RoleName roleName, List<PermissionType> permissionTypes) {
		for (PermissionType pt : permissionTypes) {
			addPermission(roleName, pt);
		}
		return getRole(roleName).getPermissions();
	}

	public Permission getPermission(PermissionType permissionType) {
		TypedQuery<Permission> permissionQuery = em.createNamedQuery(Permission.FIND_PERMISSION, Permission.class);
		permissionQuery.setParameter("permissionName", permissionType);
		return permissionQuery.getSingleResult();
	}

	public Role getRole(RoleName roleName) {
		TypedQuery<Role> roleQuery = em.createNamedQuery(Role.FIND_ROLE, Role.class);
		roleQuery.setParameter("roleName", roleName);
		return roleQuery.getSingleResult();
	}

	public Role removePermission(RoleName roleName, PermissionType permissionType) {
		Role role = getRole(roleName);

		Permission permission = getPermission(permissionType);

		role.removePermission(permission);

		return role;
	}

	public List<Permission> removePermissions(RoleName roleName, List<PermissionType> permissionTypes) {
		permissionTypes.forEach(pt -> removePermission(roleName, pt));
		return getRole(roleName).getPermissions();
	}

	public List<Role> findAllRoles() {
		TypedQuery<Role> query = em.createNamedQuery(Role.FIND_ALL_ROLES, Role.class);
		return query.getResultList();
	}

	public List<Permission> getPermissions(RoleName roleName) {
		return getRole(roleName).getPermissions();
	}

	public List<Permission> findAllPermissions() {
		TypedQuery<Permission> query = em.createNamedQuery(Permission.FIND_ALL_PERMISSIONS, Permission.class);
		return query.getResultList();
	}

	public Map<RoleName, List<PermissionType>> getRolePermissions() {
		Map<RoleName, List<PermissionType>> rolesPermissions = new HashMap<>();
		List<Role> roles = findAllRoles();
		roles.forEach(r -> rolesPermissions.put(r.getRoleName(),
				r.getPermissions().stream().map(p -> p.getPermissionName()).collect(Collectors.toList())));
		roles.forEach(r -> System.out.println(r.getPermissions()));
		return rolesPermissions;
	}
	
	
}

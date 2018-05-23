package edu.msg.ro.business.role.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import edu.msg.ro.business.role.dto.PermissionDTO;
import edu.msg.ro.business.role.dto.RoleDTO;
import edu.msg.ro.business.role.dto.mapper.PermissionDTOMapper;
import edu.msg.ro.business.role.dto.mapper.RoleDTOMapper;
import edu.msg.ro.persistence.role.dao.RoleDao;
import edu.msg.ro.persistence.role.entity.DuplicatePermission;
import edu.msg.ro.persistence.role.entity.Permission;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.role.entity.Role;
import edu.msg.ro.persistence.role.entity.RoleName;

@Stateless
public class RoleService {

	@EJB
	private RoleDao roleDao;

	@EJB
	private PermissionDTOMapper permissionDTOMapper;

	@EJB
	private RoleDTOMapper roleDTOMapper;

	public List<PermissionDTO> getPermissionsForRole(RoleDTO roledto) {
		List<Permission> permissions = roleDao.getPermissions(roledto.getRoleName());
		List<PermissionDTO> permissionDTOs = new ArrayList<>();
		permissions.forEach(p -> permissionDTOs.add(permissionDTOMapper.mapToDTO(p)));
		return permissionDTOs;
	}

	public List<PermissionDTO> addPermissions(RoleDTO roleDTO, List<PermissionDTO> permissionDTOs)
			throws DuplicatePermission {
		List<PermissionType> permissionTypes = new ArrayList<>();
		permissionDTOs.forEach(pt -> permissionTypes.add(pt.getPermissionType()));
		List<Permission> permissions = roleDao.addPermissions(roleDTO.getRoleName(), permissionTypes);
		List<PermissionDTO> result = new ArrayList<>();
		permissions.forEach(p -> result.add(permissionDTOMapper.mapToDTO(p)));
		return result;
	}

	public List<PermissionDTO> removePermissions(RoleDTO roleDTO, List<PermissionDTO> permissionDTOs) {
		List<PermissionType> permissionTypes = new ArrayList<>();
		permissionDTOs.forEach(pt -> permissionTypes.add(pt.getPermissionType()));
		List<Permission> permissions = roleDao.removePermissions(roleDTO.getRoleName(), permissionTypes);
		List<PermissionDTO> result = new ArrayList<>();
		permissions.forEach(p -> result.add(permissionDTOMapper.mapToDTO(p)));
		return result;
	}

	public List<RoleDTO> getRoles() {
		List<Role> roles = roleDao.findAllRoles();
		List<RoleDTO> rolesDTOs = new ArrayList<>();
		roles.forEach(r -> rolesDTOs.add(roleDTOMapper.mapToDTO(r)));
		return rolesDTOs;
	}

	public List<PermissionDTO> getPermissions() {
		List<Permission> permissions = roleDao.findAllPermissions();
		List<PermissionDTO> permissionDTOs = new ArrayList<>();
		permissions.forEach(p -> permissionDTOs.add(permissionDTOMapper.mapToDTO(p)));
		return permissionDTOs;
	}

	public Map<RoleName, List<PermissionType>> getRolePermissions() {
		return roleDao.getRolePermissions();
	}

	public Map<RoleName, Map<PermissionType, Boolean>> getConfiguration() {
		Map<RoleName, List<PermissionType>> rolePermission = roleDao.getRolePermissions();
		Map<RoleName, Map<PermissionType, Boolean>> config = new HashMap<>();
		rolePermission.keySet().forEach(role -> {
			config.put(role, new HashMap<>());
			for (PermissionType permission : PermissionType.values()) {
				if (rolePermission.get(role).contains(permission)) {
					config.get(role).put(permission, true);
				} else {
					config.get(role).put(permission, false);
				}
			}
		});
		return config;
	}

	public void saveConfig(Map<RoleName, Map<PermissionType, Boolean>> configuration) {
		for (RoleName role : RoleName.values()) {
			Map<PermissionType, Boolean> permissions = configuration.get(role);
			List<PermissionType> toAddPermissions = new ArrayList<>();
			List<PermissionType> toRemovePermissions = new ArrayList<>();
			for (PermissionType p : PermissionType.values()) {
				if (permissions.get(p)) {
					toAddPermissions.add(p);
				} else {
					toRemovePermissions.add(p);
				}
			}
			roleDao.removePermissions(role, toRemovePermissions);
			roleDao.addPermissions(role, toAddPermissions);
		}
	}
}

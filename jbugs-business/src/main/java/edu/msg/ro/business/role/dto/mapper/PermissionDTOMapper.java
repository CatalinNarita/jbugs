package edu.msg.ro.business.role.dto.mapper;

import javax.ejb.Stateless;

import edu.msg.ro.business.role.dto.PermissionDTO;
import edu.msg.ro.persistence.role.entity.Permission;

@Stateless
public class PermissionDTOMapper {

	public PermissionDTO mapToDTO(final Permission permissionEntity) {
		final PermissionDTO permissionDTO = new PermissionDTO();
		permissionDTO.setDescription(permissionEntity.getDescription());
		permissionDTO.setPermissionType(permissionEntity.getPermissionName());
		return permissionDTO;
	}

	public Permission mapToEntity(final PermissionDTO permissionDTO, final Permission permissionEntity) {
		permissionEntity.setDescription(permissionDTO.getDescription());
		permissionEntity.setPermissionName(permissionDTO.getPermissionType());
		return permissionEntity;
	}

}

package edu.msg.ro.business.role.dto.mapper;

import javax.ejb.Stateless;

import edu.msg.ro.business.role.dto.RoleDTO;
import edu.msg.ro.persistence.role.entity.Role;

@Stateless
public class RoleDTOMapper {

	public RoleDTO mapToDTO(final Role roleEntity) {
		final RoleDTO roleDTO = new RoleDTO();
		roleDTO.setRoleName(roleEntity.getRoleName());
		roleDTO.setId(roleEntity.getId());
		return roleDTO;
	}

	public Role mapToEntity(final RoleDTO roleDTO, final Role roleEntity) {
		roleEntity.setRoleName(roleDTO.getRoleName());
		roleEntity.setId(roleDTO.getId());
		return roleEntity;
	}

}

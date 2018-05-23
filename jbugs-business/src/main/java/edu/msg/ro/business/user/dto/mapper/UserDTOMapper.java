package edu.msg.ro.business.user.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import edu.msg.ro.business.role.dto.RoleDTO;
import edu.msg.ro.business.role.dto.mapper.RoleDTOMapper;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.persistence.user.entity.User;

@Stateless
public class UserDTOMapper {

	@EJB
	RoleDTOMapper roleMapper;
	
	public UserDTO mapToDTO(final User userEntity) {
		final UserDTO userDTO = new UserDTO();
		userDTO.setId(userEntity.getId());
		userDTO.setFirstName(userEntity.getFirstName());
		userDTO.setLastName(userEntity.getLastName());
		userDTO.setActive(userEntity.isActive());
		userDTO.setPhoneNumber(userEntity.getPhoneNumber());
		userDTO.setEmail(userEntity.getEmail());
		userDTO.setPassword(userEntity.getPassword());
		userDTO.setUsername(userEntity.getUsername());
		List<String> rolesDTO = new ArrayList<>();
		for (int i = 0; i < userEntity.getRoles().size(); i++) {
			RoleDTO roleDTO = roleMapper.mapToDTO(userEntity.getRoles().get(i));
			rolesDTO.add(roleDTO.getRoleName().name());
		}
		userDTO.setUserRoles(rolesDTO);
		return userDTO;
	}

	public User mapToEntity(final UserDTO userDTO) {
		final User userEntity = new User();
		userEntity.setFirstName(userDTO.getFirstName());
		userEntity.setLastName(userDTO.getLastName());
		userEntity.setActive(userDTO.isActive());
		userEntity.setEmail(userDTO.getEmail());
		userEntity.setPhoneNumber(userDTO.getPhoneNumber());
		userEntity.setUsername(userDTO.getUsername());
		userEntity.setPassword(userDTO.getPassword());

		return userEntity;
	}

}

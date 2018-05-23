package edu.msg.ro.business.role.dto;

import edu.msg.ro.business.dto.AbstractDTO;
import edu.msg.ro.persistence.role.entity.RoleName;

public class RoleDTO extends AbstractDTO {

	private RoleName roleName;
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public RoleDTO() {
		super();
	}

	public RoleDTO(RoleName roleName) {
		super();
		this.roleName = roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
		RoleDTO other = (RoleDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (roleName != other.roleName)
			return false;
		return true;
	}

	

}

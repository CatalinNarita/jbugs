package edu.msg.ro.business.role.dto;

import edu.msg.ro.business.dto.AbstractDTO;
import edu.msg.ro.persistence.role.entity.PermissionType;

public class PermissionDTO extends AbstractDTO {

	private PermissionType permissionType;
	private String description;

	public PermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PermissionType permissionType) {
		this.permissionType = permissionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((permissionType == null) ? 0 : permissionType.hashCode());
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
		PermissionDTO other = (PermissionDTO) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (permissionType != other.permissionType)
			return false;
		return true;
	}

}

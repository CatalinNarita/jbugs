package edu.msg.ro.business.bug.dto.mapper;

import javax.ejb.Stateless;

import edu.msg.ro.business.bug.dto.BugDTO;
import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.bug.entity.BugSeverityType;
import edu.msg.ro.persistence.bug.entity.BugStatusType;

@Stateless
public class BugDTOMapper {

	public BugDTO mapToDTO(final Bug bugEntity) {
		final BugDTO bugDTO = new BugDTO();
		bugDTO.setId(bugEntity.getId());
		bugDTO.setTitle(bugEntity.getTitle());
		bugDTO.setDescription(bugEntity.getDescription());
		bugDTO.setVersion(bugEntity.getVersion());
		bugDTO.setFixedInVersion(bugEntity.getFixedInVersion());
		bugDTO.setTargetDate(bugEntity.getTargetDate());
		bugDTO.setSeverity(bugEntity.getSeverity().toString());
		bugDTO.setCreatedBy(bugEntity.getCreatedBy());
		bugDTO.setAssignedTo(bugEntity.getAssignedTo());
		bugDTO.setStatus(bugEntity.getStatus().toString());

		return bugDTO;
	}

	public Bug mapToEntity(final BugDTO bugDTO) {
		final Bug bugEntity = new Bug();
		bugEntity.setTitle(bugDTO.getTitle());
		bugEntity.setDescription(bugDTO.getDescription());
		bugEntity.setVersion(bugDTO.getVersion());
		bugEntity.setFixedInVersion(bugDTO.getFixedInVersion());
		bugEntity.setTargetDate(bugDTO.getTargetDate());
		bugEntity.setSeverity(BugSeverityType.fromDisplayString(bugDTO.getSeverity().toLowerCase()));
		bugEntity.setCreatedBy(bugDTO.getCreatedBy());
		bugEntity.setAssignedTo(bugDTO.getAssignedTo());
		bugEntity.setStatus(BugStatusType.fromDisplayString(bugDTO.getStatus().toLowerCase()));

		return bugEntity;
	}
}

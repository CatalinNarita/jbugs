package edu.msg.ro.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.dto.mapper.UserDTOMapper;
import edu.msg.ro.persistence.user.entity.User;

@ManagedBean(name = "UserConverter")
@FacesConverter(value = "UserConverter")
public class UserConverter implements Converter {

	@EJB
	UserDTOMapper userDTOMapper;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(value);
		return userDTOMapper.mapToEntity(userDTO);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((User) value).getUsername();
	}

}

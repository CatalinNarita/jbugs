package edu.msg.ro.validator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.user.entity.User;

@Named
@RequestScoped
@FacesValidator("UserValidator")
public class UserValidator implements Validator {

	@EJB
	private UserService userService;

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (userService.getUserByUsername(((User) value).getUsername()) == null) {
			FacesMessage msg = new FacesMessage("The user does not exist!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}

}

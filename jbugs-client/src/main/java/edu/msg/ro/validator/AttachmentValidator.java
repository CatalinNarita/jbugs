package edu.msg.ro.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator("AttachmentValidator")
public class AttachmentValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			Part file = (Part) value;
			FacesMessage msg = null;
			if (!file.getContentType().endsWith("jpg") && !file.getContentType().endsWith("jpeg")
					&& !file.getContentType().endsWith("gif") && !file.getContentType().endsWith("bmp")
					&& !file.getContentType().endsWith("png") && !file.getContentType().endsWith("pdf")
					&& !file.getContentType().endsWith("doc") && !file.getContentType().endsWith("odf")
					&& !file.getContentType().endsWith("xlsx") && !file.getContentType().endsWith("xls"))
				msg = new FacesMessage(
						"The allowed file formats are jpg, jpeg, gif, bmp, png, pdf, doc, odf, xlsx, xls");
			if (msg != null) {
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}
}

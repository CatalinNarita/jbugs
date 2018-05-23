package edu.msg.ro.converter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean(name = "DateConverter")
@FacesConverter(value = "DateConverter")
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			if (value.contains(".")) {
				List<String> values = Arrays.asList(value.split("\\."));
				value = "";
				for (int i = values.size() - 1; i >= 0; i--)
					value += values.get(i) + "/";
				value = value.substring(0, value.length() - 1);
			}
			return new Date(value);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY/MM/dd");
		return formatter.format(value);
	}

}

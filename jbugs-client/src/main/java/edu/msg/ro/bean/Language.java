package edu.msg.ro.bean;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import edu.msg.ro.business.notification.service.LanguageLocale;

@ManagedBean(name = "language")
@SessionScoped
public class Language implements Serializable {

	private Locale locale;
	
	@EJB
	private LanguageLocale languageLocale; 

	@PostConstruct
	public void init() {
		locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		languageLocale.setLocale(locale);
	}

	public Locale getLocale() {
		return locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public void changeLanguage(String language) {
		locale = new Locale(language);
		languageLocale.setLocale(locale);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

}

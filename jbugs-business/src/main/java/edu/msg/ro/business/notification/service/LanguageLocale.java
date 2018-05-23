package edu.msg.ro.business.notification.service;

import java.util.Locale;

import javax.ejb.Singleton;

@Singleton
public class LanguageLocale {
	private Locale locale = new Locale("en", "GB");

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}

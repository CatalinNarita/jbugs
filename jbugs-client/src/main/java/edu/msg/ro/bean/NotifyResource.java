package edu.msg.ro.bean;

import javax.faces.application.FacesMessage;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/notify/{id}")
public class NotifyResource {

	@OnMessage( encoders = { JSONEncoder.class })
	public FacesMessage onMessage(FacesMessage message) {
		return message;
	}

}
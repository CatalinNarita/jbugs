package edu.msg.ro.business.notification.service;

import javax.ejb.Stateless;

import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.user.entity.User;

@Stateless
public class LinkGenerator {
	private final String baseUrl = "localhost:8080/jbugs/";
	
	public String getLink(User user){
		return "users.xhtml?faces-redirect=true";
	}
	public String getLink(Bug bug){
		return "bugdetails.xhtml?id="+bug.getId()+"&faces-redirect=true";
	}
}

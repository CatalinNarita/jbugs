package edu.msg.ro.bean;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import edu.msg.ro.business.notification.service.NotificationService;

@Singleton
@Startup
public class TimerBean {
	
	@EJB
	NotificationService notificationService;
	
	@Schedule(second = "*", minute = "*", hour = "*/12")
	public void execute(){
		//notificationService.deleteOutdatedNotifications();
	}
}

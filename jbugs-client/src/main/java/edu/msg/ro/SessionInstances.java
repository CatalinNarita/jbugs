package edu.msg.ro;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class SessionInstances implements HttpSessionListener {

    private static int count;

    private static List<HttpSession> activeSessions = new ArrayList<>();

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("session created: " + event.getSession().getId());
        activeSessions.add(event.getSession());
        count++;
    }

    public static List<HttpSession> getActiveSessions() {
        return activeSessions;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("session destroyed: " + event.getSession().getId());
        activeSessions.remove(event.getSession());
        count--;
    }

    public static int getCount() {
        return count;
    }

}

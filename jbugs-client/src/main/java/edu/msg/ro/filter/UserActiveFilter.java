package edu.msg.ro.filter;


import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.user.entity.User;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/welcome.xhtml","/users.xhtml","/bugs.xhtml","/notifications.xhtml","/bugDetails.xhtml"})
public class UserActiveFilter implements Filter {

    @EJB
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession httpSession = httpRequest.getSession(false);



            String requestUrl = httpRequest.getRequestURI();

            if(isLoggedInButDeactivated(httpSession) && !requestUrl.contains("login")){
                httpSession.invalidate();
                request.getRequestDispatcher("/login.xhtml").forward(request,response);
            }else{
                chain.doFilter(request,response);
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isLoggedInButDeactivated(HttpSession httpSession){
        return httpSession != null
                && httpSession.getAttribute("username") != null
                && !userService.getUserByUsername((String)httpSession.getAttribute("username")).isActive();
    }

}

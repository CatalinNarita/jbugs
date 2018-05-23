package edu.msg.ro.filter;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.role.entity.PermissionType;

@WebFilter(filterName = "UserManagementFilter", urlPatterns = { "/users.xhtml" })
public class UserManagmentFilter implements Filter {

	@EJB
	UserService userService;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean hasUserManagement = false;

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession httpSession = httpRequest.getSession(false);

		try {
			String username = (String) httpSession.getAttribute("username");
			if (username != null) {
				hasUserManagement = userService.hasPermission(username, PermissionType.USER_MANAGEMENT);
			}
			if (hasUserManagement) {
				chain.doFilter(request, response);
			} else {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/welcome.xhtml");
			}
		} catch (Exception e) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/welcome.xhtml");
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

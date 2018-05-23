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
import edu.msg.ro.persistence.role.entity.RoleName;

@WebFilter(filterName = "RoleManagementFilter", urlPatterns = { "/roles.xhtml" })
public class RoleManagementFilter implements Filter {

	@EJB
	UserService userService;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String username = "";
		boolean validUsername = false;
		boolean hasPermissionManagment = false;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession httpSession = httpRequest.getSession(false);
		try {
			Object obj = httpSession.getAttribute("username");
			if (obj == null) {
				validUsername = false;
			} else {
				validUsername = true;
				username = (String) obj;
			}
			if (validUsername) {

				hasPermissionManagment = userService.hasPermission(username, PermissionType.PERMISSON_MANAGEMENT)
						|| userService.hasRole(username, RoleName.ADM);
			}
			if (validUsername && hasPermissionManagment) {
				chain.doFilter(request, response);
			} else {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/welcome.xhtml");
			}
		} catch (NullPointerException e) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/welcome.xhtml");
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

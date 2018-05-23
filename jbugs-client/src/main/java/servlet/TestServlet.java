package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.msg.ro.business.notification.service.LinkGenerator;
import edu.msg.ro.business.notification.service.NotificationMessageGenerator;
import edu.msg.ro.business.role.service.RoleService;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.bug.dao.BugDao;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.role.dao.RoleDao;
import edu.msg.ro.persistence.user.dao.UserDao;
import edu.msg.ro.persistence.user.entity.NotificationType;

@WebServlet(urlPatterns = { "/TestServlet" })
public class TestServlet extends HttpServlet {

	@EJB 
	NotificationMessageGenerator msg;
	@EJB
	private UserService userService;
	@EJB
	private UserDao dao;
	@EJB
	private RoleService roleService;
	@EJB
	private RoleDao roleDao;
	@EJB
	private BugDao bugDao;
	@EJB 
	private LinkGenerator linkGenerator;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		final String firstName = "John_" + LocalDateTime.now();
		// userService.saveNewUser(firstName, "Doe");

		final List<UserDTO> doeUsers = userService.getUserByLastName("Doe");

		response.setContentType("text/html;charset=UTF-8");
		/*try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Test EJB Bean New</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("User with lastname  'Doe' are:<br>");
			try {
				out.println(msg.generate(dao.getUserByUserName("admin"),NotificationType.WELCOME_NEW_USER));
				out.println("<br>");
				out.println(msg.generate(dao.getUserByUserName("admin"),NotificationType.USER_DEACTIVATED));
				out.println("<br>");
				out.println(msg.generate(dao.getUserByUserName("admin"),NotificationType.BUG_CLOSED));
				out.println("<br>");
				out.println(msg.generate(dao.getUserByUserName("admin"),dao.getUserByUserName("user1")));
				out.println("<br>");
				out.println(msg.generate(bugDao.findById(1L),bugDao.findById(2L)));
				out.println("<br>");
				out.println(msg.generate(bugDao.findById(1L),NotificationType.BUG_UPDATED));
				out.println("<br>");
				out.println(msg.generate(bugDao.findById(1L),NotificationType.BUG_CLOSED));
				out.println("<br>");
				out.println(msg.generate(bugDao.findById(1L),BugStatusType.INFO_NEEDED));
				out.println("<br>");
				out.println(msg.generate(bugDao.findById(1L),BugStatusType.INFO_NEEDED));
				out.println("<br>");
				out.println(linkGenerator.getLink(bugDao.findById(1L)));
				out.println("<br>");
				out.println(linkGenerator.getLink(dao.getUserByUserName("admin")));

			} catch (Exception e) {
				out.println(e.getMessage());

			}

			out.println("</body>");
			out.println("</html>");
		}*/
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}

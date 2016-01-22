package servlets;

import models.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/verifyUser")
public class VerifyUserServlet extends HttpServlet {

//    private static final String INSERT_MEMBER = "insert into member values (?, ?, ?, ?)";
    private static final String GET_USER = "select * from acme.user as u where u.username=?";

    @Resource(lookup = "jdbc/acme")
    private DataSource ds;

//    @Inject
//    private SAGroup sagroup;
//    @Inject
//    private Member member;

    @Inject
    private User user;

    private ResultSet rs;
    RequestDispatcher rd = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

//		System.out.println(">>> member: " + member 
//				+ ", class = " + member.getClass().getName());
        System.out.println(">>> username: " + user
                + ", class = " + user.getClass().getName());

//		sagroup.add(member);
        //sagroup.add();
        /*
		Member m = new Member();
		System.out.println(">>> member = " + m.getClass().getName());
		sagroup.add(member);
         */
        User user = new User();
        User user2 = new User();
        user = (User) req.getAttribute("user");
        System.out.println(">>> User: username = " + user.getUsername() + ", password = " + user.getPassword());
        System.out.println(">>> user = " + user.getClass().getName());

        /*
		Member member = (Member)req.getAttribute("member");
		member.setMatricId(req.getParameter("matric_id"));
		member.setName(req.getParameter("name"));
		member.setEmail(req.getParameter("email"));
		member.setGroupId(req.getParameter("group_id"));
         */

 /*
		HttpSession session = req.getSession();
		SAGroup group = (SAGroup)session.getAttribute("sagroup");
		if (null == group) {
			group = new SAGroup();
			group.setName(member.getGroupId());
			session.setAttribute("sagroup", group);
		} */
//		System.out.println(">>> members = " + sagroup.getMembers());
        /*
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(INSERT_MEMBER);
			ps.setString(1, member.getMatricId());
			ps.setString(2, member.getName());
			ps.setString(3, member.getEmail());
			ps.setString(4, member.getGroupId());
			ps.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR
					, ex.getMessage());
			return;
		}
         */
        try (Connection conn = ds.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(GET_USER);
            ps.setString(1, user.getUsername());
//			ps.executeUpdate();
            rs = ps.executeQuery();
            while (rs.next()) {
                user2 = new User(rs.getString(2), rs.getString(4), rs.getString(3), rs.getInt(5));
            }
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            return;
        } finally {

            System.out.println(">>> User2: username = " + user2.getUsername() + ", password = " + user2.getPassword());
            
            String messageText = "Temp";

            if (user.getPassword().equals(user2.getPassword())) {
                // login successful
                session.setAttribute("loginuser", user2);
//                messageText = "Login successful";
//                req.setAttribute("message", messageText);
//                rd = req.getRequestDispatcher("lounge.jsp");
                rd = req.getRequestDispatcher("listGames");
            } else {
                // login unsucessful
                messageText = "Login incorrect";
                req.setAttribute("message", messageText);
                rd = req.getRequestDispatcher("index.jsp");
            }
        }

//		req.getRequestDispatcher("group.jsp")
//				.forward(req, resp);
        rd.forward(req, resp);

    }

}

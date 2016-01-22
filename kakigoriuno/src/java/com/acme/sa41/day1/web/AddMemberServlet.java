package com.acme.sa41.day1.web;

import java.io.IOException;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/add")
public class AddMemberServlet extends HttpServlet {

	private static final String INSERT_MEMBER = "insert into member values (?, ?, ?, ?)";

	@Resource(lookup = "jdbc/acme") private DataSource ds;

	@Inject private SAGroup sagroup;
	@Inject private Member member;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		System.out.println(">>> member: " + member 
				+ ", class = " + member.getClass().getName());

		sagroup.add(member);

		//sagroup.add();
		/*
		Member m = new Member();
		System.out.println(">>> member = " + m.getClass().getName());
		sagroup.add(member);
		*/

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

		
		System.out.println(">>> members = " + sagroup.getMembers());
		
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
		
		req.getRequestDispatcher("group.jsp")
				.forward(req, resp);

	}
	
}

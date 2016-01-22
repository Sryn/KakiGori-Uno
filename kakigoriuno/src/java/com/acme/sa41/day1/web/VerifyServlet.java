package com.acme.sa41.day1.web;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

	@Inject private Member member;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String matricId = req.getParameter("matric_id");
		if (isNull(matricId)) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		member.setMatricId(req.getParameter("matric_id"));
		member.setName(req.getParameter("name"));
		member.setEmail(req.getParameter("email"));
		member.setGroupId(req.getParameter("group_id"));

		req.setAttribute("member", member);

		req.getRequestDispatcher("add")
				.forward(req, resp);
	}

	private boolean isNull(String msg) {
		return ((null == msg) || (msg.trim().length() <= 0));
	}
	
}

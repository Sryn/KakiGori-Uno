package servlets;

import models.User;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/verifyLogin")
public class VerifyLoginServlet extends HttpServlet {

//	@Inject private Member member;
//    @Inject
    private User user = new User();

    RequestDispatcher rd = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        if (null == session.getAttribute("loginuser")) {
            String username = req.getParameter("username");
            if (isNull(username)) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return;
                req.setAttribute("message", "Please fill in username and password");

                rd = req.getRequestDispatcher("index.jsp");
            } else {
//		member.setMatricId(req.getParameter("matric_id"));
//		member.setName(req.getParameter("name"));
//		member.setEmail(req.getParameter("email"));
//		member.setGroupId(req.getParameter("group_id"));
                user.setUsername(req.getParameter("username"));
                user.setPassword(req.getParameter("password"));

                req.setAttribute("user", user);
                rd = req.getRequestDispatcher("verifyUser");
            }

        } else {
            rd = req.getRequestDispatcher("listGames");
        }

//        req.getRequestDispatcher("verifyUser")
//                .forward(req, resp);
        rd.forward(req, resp);
    }

    // Method to handle GET method request.
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    private boolean isNull(String msg) {
        return ((null == msg) || (msg.trim().length() <= 0));
    }

}

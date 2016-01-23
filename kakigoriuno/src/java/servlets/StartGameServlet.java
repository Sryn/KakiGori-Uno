/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Game;
import models.GamesMap;
import models.User;

/**
 *
 * @author Sryn
 */
//@WebServlet(name = "StartGameServlet", urlPatterns = {"/StartGameServlet"})
@WebServlet("/startGame")
public class StartGameServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Inject
    GamesMap gamesMap;

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String strMapGameId, loginUserName;
        Game currentGame;
        User loginUser;
        int currGameNoOfPlayers;

        HttpSession session = req.getSession();
        loginUser = (User) session.getAttribute("loginuser");
        loginUserName = loginUser.getUsername();

        strMapGameId = req.getParameter("mapGameId");
        System.out.println("> loginUser=" + loginUserName + " reqPar mapGameId=" + strMapGameId);
        if (null == strMapGameId) {
            // user was autoforwarded here
            strMapGameId = (String) req.getAttribute("mapGameId");
            System.out.println("> loginUser=" + loginUserName + " reqAtt mapGameId=" + strMapGameId);
        }

        Long lonMapGameId = Long.valueOf(strMapGameId);
        currentGame = gamesMap.get(lonMapGameId);
        currGameNoOfPlayers = currentGame.getGamePlayers().size();

        System.out.println(">>>> current game started: " + currentGame);
        if ((currGameNoOfPlayers > 1) && (currGameNoOfPlayers <= 10)) {
            System.out.println(">> before change mapGameId=" + strMapGameId + " status=" + currentGame.getGameStatus());
            if (currentGame.isWaiting()) {
                currentGame.startGame();
            }
        } else {
            req.getRequestDispatcher("joinGame").forward(req, resp);
        }

        System.out.println(">> after  change mapGameId=" + strMapGameId + " status=" + currentGame.getGameStatus());

//        req.setAttribute("mapGameId", strMapGameId);
//        req.getRequestDispatcher("playSubGame").forward(req, resp);
//        req.getRequestDispatcher("playSubGame");
        session.setAttribute("mapGameId", strMapGameId);
        resp.setHeader("Refresh", "0; playSubGame");

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StartGameServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StartGameServlet at " + req.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Card;
import models.Game;
import models.GamesMap;
import models.Player;
import models.SubGame;
import models.User;

/**
 *
 * @author Sryn
 */
@WebServlet(name = "drawCard", urlPatterns = {"/drawCard"})
public class drawCard extends HttpServlet {

    @Inject
    GamesMap gamesMap;

//    RequestDispatcher rd = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        String strMapGameId;
        Long lonMapGameId;
        Game currentGame;
        SubGame currentSubGame;
        Player currentLoginPlayer;
        Boolean doDraw = true;
        Card drawnCard;

        User loginUser = (User) session.getAttribute("loginuser");
        strMapGameId = session.getAttribute("mapGameId").toString();
        lonMapGameId = Long.valueOf(strMapGameId);
        currentGame = gamesMap.get(lonMapGameId);
        currentSubGame = currentGame.getCurrentSubGame();
        currentLoginPlayer = currentSubGame.getPlayerFromUserObject(loginUser);

        System.out.println(">>> In drawCard with Player " + currentLoginPlayer.getPlayer().getUsername());
        
        if (currentSubGame.getDrawPile().getListOfCards().isEmpty()) {
            if (currentSubGame.getDiscardPile().size() > 1) {
                currentSubGame.refillDrawPile();
                doDraw = true;
            } else {
                doDraw = false;
            }
        } else {
            doDraw = true;
        }
        
        if(doDraw) {
            drawnCard = currentSubGame.getDrawPile().drawCard();
            currentLoginPlayer.getHand().addCard(drawnCard);
            System.out.println(">>> drawCard OK: " + drawnCard.getCardName());
        }

//        resp.setHeader("Refresh", "0; playSubGame");
//        req.setAttribute("doDraw", doDraw);
        req.setAttribute("adcfdp", doDraw);
        req.getRequestDispatcher("playSubGame").forward(req, resp);

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet drawCard</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet drawCard at " + req.getContextPath() + "</h1>");
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

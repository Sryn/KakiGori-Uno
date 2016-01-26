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
import models.Card;
import models.Game;
import models.Game.GameStyle;
import models.GamesMap;
import models.Player;
import models.SubGame;
import models.User;
import static utilities.Utilities.pairOfCardMatchDeterminator;

/**
 *
 * @author Sryn
 */
@WebServlet(name = "playCard", urlPatterns = {"/playCard"})
public class playCard extends HttpServlet {

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

        int currentPoints;
        String strMapGameId;
        Long lonMapGameId;
        Game currentGame;
        SubGame currentSubGame;
        Player currentLoginPlayer, nextPlayer;
        Boolean doDraw = true, loadError = false;
        Card chosenCard = null, discardPileTopCard;

        User loginUser = (User) session.getAttribute("loginuser");
        strMapGameId = (String) session.getAttribute("mapGameId");
        lonMapGameId = Long.valueOf(strMapGameId);
        currentGame = gamesMap.get(lonMapGameId);
        currentSubGame = currentGame.getCurrentSubGame();
        currentLoginPlayer = currentSubGame.getPlayerFromUserObject(loginUser);

        System.out.println(">>> In playCard with cardChoice = " + req.getParameter("cardChoice")
                + " and reqPar adcfdp=" + req.getParameter("adcfdp") + " with Player " + currentLoginPlayer.getPlayer().getUsername());

        if (null == req.getParameter("cardChoice")) {
            // no choice made so go back
            req.setAttribute("adcfdp", req.getParameter("adcfdp"));
            req.getRequestDispatcher("playSubGame").forward(req, resp);
        } else {
            chosenCard = currentLoginPlayer.getHand().getCardById(Long.parseLong(req.getParameter("cardChoice")));
            System.out.println(">>> In playCard with cardChoice = " + chosenCard.getCardName());
        }

        discardPileTopCard = currentSubGame.getDiscardPile().getTopCard();

        if (chosenCard != null && discardPileTopCard != null) {
            loadError = true;
            
            if (pairOfCardMatchDeterminator(chosenCard, discardPileTopCard)) {
                loadError = false;
                                
                // remove chosenCard from loginPlayer hand
                currentLoginPlayer.getHand().removeCard(chosenCard);
                // put chosenCard to top of discardPile
                currentSubGame.getDiscardPile().addCardToTop(chosenCard);

                // check if this was currentPlayer's last card, which means s/he's the subGame winner
                if (currentLoginPlayer.getHand().getListOfCards().isEmpty()) {
                    // subGame winner
                    System.out.println(">>> Player " + currentLoginPlayer.getPlayer().getUsername() + " is the winner of this subgame");
                    currentSubGame.setSubGameWinner(currentLoginPlayer);
                    currentSubGame.finishSubGame();
                    currentGame.setupGame();
                    if(currentGame.getGameStyle().equals(GameStyle.LOWESTPOINTS)) {
                        // winner gets zero points
                        // other players get points equal to their hand points
                        for(Player aPlayer: currentSubGame.getSubGamePlayers()) {
                            currentPoints = aPlayer.addUpHandPoints();
                            aPlayer.setGamePoints(currentPoints);
                        }
                    } else if(currentGame.getGameStyle().equals(GameStyle.FIRSTTO500)) {
                        // winner gets the total of all other players' hand points
                        // other players get zero points
                        currentPoints = 0;
                        for(Player aPlayer: currentSubGame.getSubGamePlayers()) {
                            currentPoints += aPlayer.addUpHandPoints();                            
                            aPlayer.setGamePoints(0);
                        }
                        currentLoginPlayer.setGamePoints(currentPoints);
                    } else {
                        System.out.println("### ERROR: gamestyle=" + currentGame.getGameStyle().toString());                        
                    }
                    
                    // add up all players points to respective game users tempPoints
                    for(Player aPlayer: currentSubGame.getSubGamePlayers()) {
                        for(User aUser: currentGame.getGamePlayers()) {
                            if(aPlayer.getPlayer().equals(aUser)) {
                                currentPoints = aPlayer.getGamePoints() + aUser.getTempPoints();
                                aUser.setTempPoints(currentPoints);
                            }
                        }
                    }
                    
//                    req.setAttribute("adcfdp", false);
                    req.getRequestDispatcher("joinGame").forward(req, resp);
                } else {
                    // process consequences of the chosenCard and get nextPlayer
                    nextPlayer = currentSubGame.processCardPlayedAndGetNextPlayer(chosenCard); // .getNextPlayer(currentLoginPlayer);
                    
//                // set nextPlayer as new currentPlayer
                    currentSubGame.setCurrentPlayer(nextPlayer);
                    
//                    req.setAttribute("adcfdp", "false");
                    req.setAttribute("adcfdp", false);
                    req.getRequestDispatcher("playSubGame").forward(req, resp);
                }
            } else {
                loadError = true;
            }
        } 
        
        if (loadError) {
            // ERROR: chosenCard do not match the top card of discardPile
            System.out.println("### chosenCard do not match the top card of discardPile");
            // go back to playSubGame
            req.setAttribute("adcfdp", req.getParameter("adcfdp"));
            req.getRequestDispatcher("playSubGame").forward(req, resp);
        }

//        resp.setHeader("Refresh", "0; playSubGame");
//        req.setAttribute("doDraw", doDraw);
//        req.getRequestDispatcher("playSubGame").forward(req, resp);
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet playCard</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet playCard at " + req.getContextPath() + "</h1>");
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.*;
import models.Card.Action;
import models.Card.Colour;
import models.SubGame.Direction;
//import models.Card;
//import models.CardList;
//import models.Game;
//import models.GamesMap;
//import models.Player;
//import models.SubGame;
//import models.User;

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
     * @param req servlet request
     * @param resp servlet response
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
        int currGameNoOfPlayers, roundNo;

        List<User> currentGamePlayers;
        List<SubGame> listSubGameRounds = null;

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
        currentGamePlayers = currentGame.getGamePlayers();
        currGameNoOfPlayers = currentGame.getGamePlayers().size();

        System.out.println(">>>> current game started: " + currentGame);
        if ((currGameNoOfPlayers > 1) && (currGameNoOfPlayers <= 10)) {
            System.out.println(">> before change mapGameId=" + strMapGameId + " status=" + currentGame.getGameStatus());

            // to make new subGame
            // automatically do setup process once a player has
            //  either clicked 'Start Game'
            //  or previous subGame has 'finished' (?)
            // update roundNo
            // if no subgames, therefore new Game
            if (null == currentGame.getSubGameList()) {
                // Game just started, therefore round no 1
                roundNo = 1;
                listSubGameRounds = new ArrayList();
                currentGame.setSubGameList(listSubGameRounds);
            } else {
                // new round
                roundNo = currentGame.getSubGameList().size() + 1;
                listSubGameRounds = currentGame.getSubGameList();
            }
            
            System.out.println(">> startGame roundNo=" + roundNo);
            
            // check if new round has been prepared, if not, prepare new round
            if (listSubGameRounds.size() < roundNo) {
                
                Player previousSubRoundWinner = null;
                
                // get gamePlayers list from Game and populate subGamePlayers list
                List<Player> newRoundPlayers = new ArrayList<>();
                for (User aUser : currentGamePlayers) {
                    Player aPlayer = new Player(aUser);
                    newRoundPlayers.add(aPlayer);
                }

                // make new subGame            
                SubGame currentSubGame = new SubGame(currentGame, newRoundPlayers, roundNo);

                // add new subGame to list of SubGames
//                listSubGameRounds.add(currentSubGame);
                if(!listSubGameRounds.isEmpty()) {
                    User previousWinnerUser = listSubGameRounds.get(0).getSubGameWinner().getPlayer();
                    
                    for(Player aPlayer: newRoundPlayers) {
                        if(aPlayer.getPlayer() == previousWinnerUser);
                        previousSubRoundWinner = aPlayer;
                        break;
                    }
                    
//                    previousSubRoundWinner = new Player(previousWinnerUser);
                }

                listSubGameRounds.add(0, currentSubGame);

//                System.out.println(currentSubGame.getDrawPile().toString());
                currentSubGame.getDrawPile().shuffleCards();

                System.out.println(">> After shuffling");
//                System.out.println(currentSubGame.getDrawPile().toString());

                if(roundNo == 1)
                    currentSubGame.setCurrentPlayer(getCurrentPlayer(currentGame, roundNo)); // determine 1st player
                else
                    currentSubGame.setCurrentPlayer(previousSubRoundWinner);

                System.out.println(">> FirstPlayer is " + currentSubGame.getCurrentPlayer().getPlayer().getUsername());
                
                // return all cards to drawPile and shuffle again
                // done at the end of the previous step
                System.out.println(">> drawPile size = " + currentSubGame.getDrawPile().size()); // .getListOfCards().size());

                // In subGamePlayers list, move all players to the left (earlier/lower)
                //  of 1st player to the end of the list
                System.out.println(">> Before reordering players\n" + currentSubGame.getPlayersListText());
                if(currentSubGame.getSubGamePlayers().get(0) != currentSubGame.getCurrentPlayer()) {
                    currentSubGame.movePlayersBeforeFirstPlayerToEndOfList();                    
                    System.out.println(">>  After reordering players\n" + currentSubGame.getPlayersListText());
                } else {
                    System.out.println(">>  Not reordering players\n" + currentSubGame.getPlayersListText());
                }

            // shuffle out 7 cards to each player one at a time in a clockwise
            //  (increasing index) fashion starting from the 1st player
                System.out.println(">> Before dealing:\n" + currentSubGame.listAllCardListSizes());
                currentSubGame.dealOutSevenCardsToEveryPlayer();
                System.out.println(">> After  dealing:\n" + currentSubGame.listAllCardListSizes());
                
                // temporary test of empty drawPile
//                System.out.println("*** temporary test of empty drawPile");
//                System.out.println("**** before drawPile    size = " + currentSubGame.getDrawPile().getListOfCards().size());
//                System.out.println("**** before discardPile size = " + currentSubGame.getDiscardPile().getListOfCards().size());
//                currentSubGame.moveAllCards(currentSubGame.getDrawPile(), currentSubGame.getDiscardPile());
//                System.out.println("**** after  drawPile    size = " + currentSubGame.getDrawPile().getListOfCards().size());
//                System.out.println("**** after  discardPile size = " + currentSubGame.getDiscardPile().getListOfCards().size());

            // draw a card from drawPile to discardPile
            //  if drawn card is wild or wild4, add back to drawPile, shuffle drawPile & draw again
                Boolean gotValidFirstDiscardCard = false, boolResult = true;
                Card firstDiscardCard;
                
                while(!gotValidFirstDiscardCard && boolResult) {
                    System.out.println(">> Before drawing first card to discardPile:\n" + currentSubGame.showDrawAndDiscardPilesTopCards());
                    boolResult = currentSubGame.drawOneCardFromDrawPileToDiscardPile();
                    
                    if(boolResult)
                        System.out.println("### Success");
                    else
                        System.out.println("### Error");
    //                System.out.println("**** after2 drawPile    size = " + currentSubGame.getDrawPile().getListOfCards().size());
    //                System.out.println("**** after2 discardPile size = " + currentSubGame.getDiscardPile().getListOfCards().size());
    
                    firstDiscardCard = currentSubGame.getDiscardPile().getListOfCards().get(0);
                    System.out.println(">> firstDiscardCard = " + firstDiscardCard.toString());
                    
                    if(firstDiscardCard.getCardAction().equals(Action.WILD) || firstDiscardCard.getCardAction().equals(Action.WILD_DRAW4)) {
                        // invalid first discard card
                        gotValidFirstDiscardCard = false;
                        
                        // return Wild_Draw4 back to drawPile, shuffle drawPile and repeat draw first discard card process
                        currentSubGame.moveAllCards(currentSubGame.getDiscardPile(), currentSubGame.getDrawPile());
                        currentSubGame.getDrawPile().shuffleCards();
                    } else {
                        gotValidFirstDiscardCard = true;
                    }
    
                    System.out.println(">> After  drawing first card to discardPile:\n" + currentSubGame.showDrawAndDiscardPilesTopCards());
                }
                
            //  if drawn card is a number card, 1st player becomes next player
            //  if drawn card is an action/symbol card, act upon that card towards
            //   the 1st player to determine the next player (will become new 1st player)
                                
                if(null != currentSubGame.getDiscardPile().getTopCard()) {
                    firstDiscardCard = currentSubGame.getDiscardPile().getTopCard();
                    Colour discardCardColour = firstDiscardCard.getCardColour();
                    Action switchAction = firstDiscardCard.getCardAction();
                    Player lastPlayer = currentSubGame.getLastPlayer();
                    
                    currentSubGame.getColourList().add(discardCardColour);
                    
                    switch(switchAction) {
                        case REVERSE:
                            currentSubGame.getDirectionList().add(Direction.ANTICLOCKWISE);
                            // currentPlayer will change
                            currentSubGame.setCurrentPlayer(currentSubGame.getNextPlayer(currentSubGame.getCurrentPlayer()));
                            break;
                        case NUMBER:
                            currentSubGame.getDirectionList().add(Direction.CLOCKWISE);
                            break;
                        case SKIP:
                            currentSubGame.getDirectionList().add(Direction.CLOCKWISE);
                            // currentPlayer will change
                            currentSubGame.setCurrentPlayer(currentSubGame.getAfterSkipPlayer(lastPlayer));
                            break;
                        case DRAW2:
                            currentSubGame.getDirectionList().add(Direction.CLOCKWISE);
                            // draw 2 cards from drawPile and give to currentPlayer
                            currentSubGame.givePlayer2Cards(currentSubGame.getCurrentPlayer());
                            // currentPlayer will change
                            currentSubGame.setCurrentPlayer(currentSubGame.getAfterDraw2Player(lastPlayer));
                            break;
                        default: 
                            System.out.println("### Error: Illegal switchAction = " + switchAction.toString());
                            break;
                    }
                    
                }

                System.out.println(">> Current subGame / Round currentPlayer = " + currentSubGame.getCurrentPlayer().getPlayer().toString());
                System.out.println(">> Latest all cardList sizes:\n" + currentSubGame.listAllCardListSizes());
                
//                session.setAttribute("currentSubGame", currentSubGame.getSubGameId());
                currentGame.setCurrentSubGame(currentSubGame);

                // set subGameStatus = "started"            
                currentSubGame.startSubGame();
                
            }
            
            if (currentGame.isWaiting()) {
                currentGame.startGame();
            }
            
            // wait for current player to 'add' valid card from his/her hand to discardPile
            // this happens in PlaySubGameServlet
        } else {
            // somehow a user left the game right when it was started
            req.getRequestDispatcher("joinGame").forward(req, resp);
        }

        System.out.println(">> after  change mapGameId=" + strMapGameId + " status=" + currentGame.getGameStatus());

//        req.setAttribute("mapGameId", strMapGameId);
//        req.getRequestDispatcher("playSubGame").forward(req, resp);
//        req.getRequestDispatcher("playSubGame");
        session.setAttribute("mapGameId", strMapGameId);
//        resp.setHeader("Refresh", "0; playSubGame");
//        req.setAttribute("doDraw", false);
        req.setAttribute("adcfdp", false);
        req.getRequestDispatcher("playSubGame").forward(req, resp);

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

    private Player getCurrentPlayer(Game currentGame, int roundNo) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        System.out.println(">>> In startGame getCurrentPlayer currentGame=" + currentGame.toString() + " roundNo=" + roundNo);

        if (roundNo == 1) {
            System.out.println(">>> In startGame getCurrentPlayer roundNo == 1");
            // determine 1st player
            // Note: there is no 'dealer' player
            // automatically determine 1st player by
            //  if 1st subgame, then draw one card each from drawPile,
            //   player with highest number card becomes 1st player
            //   draws will result in the draw players drawing again
            //   (Action/Symbol cards ignored)
            //  if not 1st subgame, previous subGame winner becomes 1st player

            SubGame currentRound = currentGame.getSubGameList().get(0);
            Card currentCard;
            Boolean firstPlayerFound = false;
            Player currentHighestDrawPlayer = null;
            int highestNumber = 0, currentNumber;
            List<Player> firstRoundPlayers = currentRound.getSubGamePlayers();
            List<Player> currentDrawPlayers = new ArrayList(firstRoundPlayers);
            Collections.copy(currentDrawPlayers, firstRoundPlayers);
            List<Player> drawPlayers = new ArrayList(); // this will reduce same card value players list

            while (!firstPlayerFound) {
                // check if drawPile has enough cards to be drawn
                if (currentRound.getDrawPile().getListOfCards().size() >= currentDrawPlayers.size()) {
                    //get one card each from drawPile
                    for (Player aPlayer : currentDrawPlayers) {
                        aPlayer.getHand().addCard(currentRound.getDrawPile().drawCard());
                    }

                    // determine who has the highest number card
                    for (Player aPlayer : currentDrawPlayers) {
                        List<Card> aPlayerHand = aPlayer.getHand().getListOfCards();
                        currentCard = aPlayerHand.get(aPlayerHand.size() - 1); // get latest added card
                        if (currentCard.getCardAction().equals(Action.NUMBER)) {
                            currentNumber = aPlayerHand.get(aPlayerHand.size() - 1).getCardValue();
                            if (currentNumber == highestNumber) {
                                // in case 1st drawn card is value 0
                                if (null == currentHighestDrawPlayer) {
                                    currentHighestDrawPlayer = aPlayer;
                                    drawPlayers.add(aPlayer);
                                } else {
                                    // there's more than one player drawing the highest value
                                    currentHighestDrawPlayer = aPlayer;
                                    drawPlayers.add(aPlayer);
                                }
                            } else if (currentNumber > highestNumber) {
                                highestNumber = currentNumber;
                                currentHighestDrawPlayer = aPlayer;
                                drawPlayers.clear();
                                drawPlayers.add(aPlayer);
                            }
                        }
                    }

                    if (drawPlayers.isEmpty()) {
                        // nobody drew a number
                        System.out.println(">> nobody drew a number to determine 1st player");
                        // repeat
                        highestNumber = 0;
                    } else if (drawPlayers.size() == 1) {
                        // there's one winner
                        firstPlayerFound = true;

                        // move all cards from each player's hand back to the drawPile
//                        moveBackAllCardsToDrawPile(firstRoundPlayers, currentRound);
                        System.out.println(">> Player " 
                                + currentHighestDrawPlayer.getPlayer().getUsername() 
                                + " got the highest number card: " 
                                + highestNumber);
                        currentRound.moveBackAllCardsToDrawPile();

                    } else if (drawPlayers.size() > 1) {
                        // there's more than one player that drew the highest value card
                        // limit the next round of draws to those players only
                        System.out.println(">> " + drawPlayers.size() 
                                + " players got the highest number card: " 
                                + highestNumber);
                        highestNumber = 0;
                        currentDrawPlayers.clear();
                        
                        // error: java.lang.IndexOutOfBoundsException: Source does not fit in dest
//                        Collections.copy(currentDrawPlayers, drawPlayers); 

                        while(!drawPlayers.isEmpty())
                            currentDrawPlayers.add(drawPlayers.remove(0));
                        
                        drawPlayers.clear();
                    }
                } else {
//                    moveBackAllCardsToDrawPile(firstRoundPlayers, currentRound);
                    currentRound.moveBackAllCardsToDrawPile();

                }
            }

            return currentHighestDrawPlayer;

        } else { // shouldn't go in here anymore
            System.out.println(">>> In startGame getCurrentPlayer roundNo != 1");
            // get winner of previous round
//            return currentGame.getSubGameList().get(roundNo - 1).getSubGameWinner();
//            int subGameListSize = currentGame.getSubGameList().size();
//            int currentSubGameIdx = subGameListSize - 1;
//            int previousSubGameIdx = currentSubGameIdx - 1;

//            return currentGame.getSubGameList().get(roundNo - 2).getSubGameWinner();
//            return currentGame.getSubGameList().get(previousSubGameIdx).getSubGameWinner();
//            return currentGame.getSubGameList().get(1).getSubGameWinner();
            return currentGame.getSubGameList().get(1).getSubGameWinner();
        }
    }

//    public void moveBackAllCardsToDrawPile(List<Player> firstRoundPlayers, SubGame currentRound) {
//        // move all cards from each player's hand back to the drawPile
//        for (Player aPlayer : firstRoundPlayers) {
//            CardList currentHand = aPlayer.getHand();
//            while (!currentHand.getListOfCards().isEmpty()) {
//                currentRound.getDrawPile().addCard(currentHand.drawCard());
//            }
//        }
//        currentRound.getDrawPile().shuffleCards();
//    }

}

package servlets;

import static utilities.Utilities.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Game;
import models.Game.GameStyle;
import models.GamesMap;
import models.SubGame;
import models.User;

@WebServlet("/joinGame")
public class JoinGameServlet extends HttpServlet {

    @Inject
    GamesMap gamesMap;

    RequestDispatcher rd = null;

    // need the ArrayList, else NullPointerException on add
    public List<Game> gamesList = new ArrayList();
//    public Map<Long, Game> gamesMap = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//        String strGameId, strTableNo;
        HttpSession session = req.getSession();
        ServletContext appScopeServlet = req.getServletContext();
//        Long lonGameId = null;

        String strMapGameId = null, TableNo, loginUserName, gameWinners = ""
                , displayWinner = "", prevSubGameWinner = "";
        Boolean gameFinished = false;

        gamesList = (List<Game>) appScopeServlet.getAttribute("gamesList");
//        gamesMap = (Map<Long, Game>) appScopeServlet.getAttribute("gamesMap");

        User loginUser;
//        User loginUser = new User();
//        Game currentGame = new Game();
        Game currentGame2;
//        List<User> currentGamePlayers = new ArrayList();
        List<User> currentGamePlayers2;
        SubGame previousGame;

        loginUser = (User) session.getAttribute("loginuser");
        loginUserName = loginUser.getUsername();
//        System.out.println(">>> loginUser=" + loginUser.toString());
        /*
        if (null == req.getParameter("gameId")) {
            // not a gameList gameId
            if (null == req.getParameter("mapGameId")) {
                // not a gameMap gameId
                // therefore an error
                System.out.println(">>> can't get req.getParameter gameId nor mapGameId");
            } else {
                // a gameMap gameId
                // do gamesMap process
                System.out.println(">>> got a req.getParameter mapGameId=" + req.getParameter("mapGameId"));

            }
        } else // a gameList gameId
         if (null == req.getParameter("mapGameId")) {
                // not a gameMap gameId
                // do gamesList process
                System.out.println(">>> got a req.getParameter gameId=" + req.getParameter("mapGameId"));

            } else {
                // a gameMap gameId
                // therefore an error
                System.out.println(">>> got both req.getParameter gameId and mapGameId");
            }

        if (null == session.getAttribute("gameId")) {
            strGameId = req.getParameter("gameId");
            lonGameId = Long.valueOf(strGameId);
            strTableNo = getIntOfSumOfLongDigits(lonGameId).toString();
            session.setAttribute("gameId", lonGameId);
            System.out.println(">>> from req strGameId = " + strGameId);
        } else {
            strGameId = session.getAttribute("gameId").toString();
            lonGameId = Long.valueOf(strGameId);
            strTableNo = getIntOfSumOfLongDigits(lonGameId).toString();
            session.setAttribute("gameId", lonGameId);
            System.out.println(">>> from session strGameId = " + strGameId);
        }

        TableNo = strTableNo;

        currentGame.setGameId(lonGameId);
        currentGamePlayers.add(loginUser);
        currentGame.setGamePlayers(currentGamePlayers);

        System.out.println(">>> gameId: " + strGameId + " @Index=" + Integer.toString(gamesList.indexOf(currentGame)));
         */
        if (null == session.getAttribute("mapGameId")) {
            strMapGameId = req.getParameter("mapGameId");
//            System.out.println(">>> from req strMapGameId = " + strMapGameId);
        } else if(null != session.getAttribute("mapGameId")) {
            strMapGameId = session.getAttribute("mapGameId").toString();
//            System.out.println(">>> from session strMapGameId = " + strMapGameId);
//        } else {
        }
            // somehow the radio button in lounge deactivated (after 5 secs)
            //  right when the loginUser pressed the button
            // therefore, go back to lounge
        if(null == strMapGameId)
            req.getRequestDispatcher("lounge.jsp").forward(req, resp);
//        }
        Long lonMapGameId = Long.valueOf(strMapGameId);
//        String strMapTableNo = getIntOfSumOfLongDigits(lonMapGameId).toString();
        session.setAttribute("mapGameId", lonMapGameId);

        currentGame2 = gamesMap.get(lonMapGameId);
        String gameInstanceName = currentGame2.toString();
        gameInstanceName = gameInstanceName.substring(12);
//        System.out.println(">>> currentGame2 = " + currentGame2.toString() + " - " + gameInstanceName);
//        System.out.println(">> currentGame2 = " + currentGame2);

        String strMapTableNo = currentGame2.getGameName();
        TableNo = strMapTableNo;
        
        if (null == currentGame2.getGamePlayers()) {
            // no gamePlayers yet
            // add gamePlayers
            currentGamePlayers2 = new ArrayList();
            currentGame2.setGamePlayers(currentGamePlayers2);
        } else {
            // get gamePlayers
            currentGamePlayers2 = currentGame2.getGamePlayers();
        }
        // add currentLoginUser to gamePlayers, if not already in there
        if (!currentGamePlayers2.contains(loginUser)) {
            boolean add = currentGamePlayers2.add(loginUser);
        }

        if (currentGame2.getGamePlayers().size() > 0) {
            // change gameStatus to waitingToStart
            if (currentGame2.isListed()) {
                currentGame2.setupGame();
            }
        }

//        System.out.println(">> outside check mapGameId=" + strMapGameId + " status=" + currentGame2.getGameStatus());
        if (currentGame2.isStarted()) {
            System.out.println(">>  inside check mapGameId=" + strMapGameId + " status=" + currentGame2.getGameStatus());
//            req.setAttribute("mapGameId", strMapGameId);
//            rd = req.getRequestDispatcher("startGame");
//            rd = req.getRequestDispatcher("playSubGame");
//            rd.forward(req, resp);
            session.setAttribute("mapGameId", strMapGameId);
            resp.setHeader("Refresh", "0; playSubGame");
        } else {
            // Set refresh, autoload time as 5 seconds
//            req.setAttribute("mapGameId", strMapGameId);
            resp.setHeader("Refresh", "5; joinGame");
        }

        String trPlayerList = "";

        int i = 1, roundsPlayed = 0, noOfSubGames = 0;
        
        if(null != currentGame2.getSubGameList()) { // game has started
            roundsPlayed = currentGame2.getSubGameList().size();
                        
        } else { // game hasn't started
            // reset users tempPoints to 0 each
            for(User aUser: currentGame2.getGamePlayers()) {
                aUser.setTempPoints(0);
            }
        }
        
        for (User aUser : currentGamePlayers2) {
            trPlayerList = trPlayerList.concat("<tr>\n");
            trPlayerList = trPlayerList.concat("<td>");
            trPlayerList = trPlayerList.concat(Integer.toString(i++));
            trPlayerList = trPlayerList.concat("</td>\n");
            trPlayerList = trPlayerList.concat("<td>");
            trPlayerList = trPlayerList.concat(aUser.getUsername());
            trPlayerList = trPlayerList.concat("</td>\n");
            trPlayerList = trPlayerList.concat("<td>");
            trPlayerList = trPlayerList.concat(Integer.toString(aUser.getTempPoints()));
            trPlayerList = trPlayerList.concat("</td>\n");
            trPlayerList = trPlayerList.concat("</tr>\n");
            
            if(aUser.getTempPoints() >= 500) {
                gameFinished = true;
                currentGame2.finishGame();
            }                
        }

        if(gameFinished) {
            gameWinners = getGameWinnerName(currentGamePlayers2, currentGame2.getGameStyle());
            
            displayWinner = displayWinner.concat("<h1>Game Winner(s): ");
            displayWinner = displayWinner.concat(gameWinners);
            displayWinner = displayWinner.concat("</h1>");
        } else {
            // display previous subgame winner, if any round has already been played
            if(null != currentGame2.getSubGameList()) {
                // there are subgames available
                // get last subgame in list
                noOfSubGames = currentGame2.getSubGameList().size();
                previousGame = currentGame2.getSubGameList().get(noOfSubGames - 1);
                if(null != previousGame.getSubGameWinner()) {
                    prevSubGameWinner = previousGame.getSubGameWinner().getPlayer().getUsername();

                    displayWinner = displayWinner.concat("<h1>Previous Round Winner is ");
                    displayWinner = displayWinner.concat(prevSubGameWinner);
                    displayWinner = displayWinner.concat("</h1>");
                }
            }
        }
        
        String startGameBtnVisibility = "";

        if ((currentGamePlayers2.size() > 1) && (currentGamePlayers2.size() <= 10)) {
            startGameBtnVisibility = startGameBtnVisibility.concat("<button type=\"submit\" name=mapGameId value=\"");
            startGameBtnVisibility = startGameBtnVisibility.concat(strMapGameId);
            startGameBtnVisibility = startGameBtnVisibility.concat("\">Start Game Round</button>\n");
        }
        
        if(gameFinished) { // don't show button
            startGameBtnVisibility = "";
        }

//        System.out.println(">>> mapGameId: " + strMapGameId + " NoOfPlayers=" + gamesMap.get(lonMapGameId).getGamePlayers().size());

        // Set resp content type
        resp.setContentType("text/html");

        String CT = getCurrentTimeString();

        PrintWriter out = resp.getWriter();
        String pageTitle = "Uno Table " + TableNo;
        String bodyTitle = "Welcome to Table " + TableNo + " " + loginUserName + "!";
        String docType
                = "<!doctype html public \"-//w3c//dtd html 4.0 "
                + "transitional//en\">\n";
        out.println(docType
                + "<html>\n"
                + "<head>"
                + "<title>" + pageTitle + "</title>"
                + "</head>\n"
                + "<body bgcolor=\"#f0f0f0\">\n"
                + "<h1 align=\"center\">" + bodyTitle + "</h1>\n"
                + "<hr>"
                + "<p>Current Time is: " + CT + "</p>\n"
                + "<a href=\"listGames\">Return to Games Lounge</a><br><br>\n"
                + "<h2>Table " + currentGame2.getGameName() + " status: " + currentGame2.getGameStatus() + "</h2>"
                + "<h2>GameStyle: " + currentGame2.getGameStyle().toString() + "</h2>"
                + showGameStyleNote(currentGame2.getGameStyle()) + "\n"
                + displayWinner
                + showRoundsPlayed(roundsPlayed)
                + "<form method=\"POST\" action=\"startGame\">\n"
                + "<table border=\"1\">\n"
                + "<tr>\n"
                + "<td>Player Index</td>\n"
                + "<td>Player UserName</td>\n"
                + "<td>Total Points</td>\n"
                + "</tr>\n"
                + trPlayerList
                + "</table><br><br>\n"
                + startGameBtnVisibility
                + "</form>\n"
                + "</body>\n"
                + "</html>\n"
                + "");

        //        req.getRequestDispatcher("clock5secs.jsp")
        //                .forward(req, resp);
    } // doPost
    
    private String getGameWinnerName(List<User> currentGamePlayers, GameStyle style) {
        String strWinnersNames = "";
        int winningPoints = 0, noOfWinners = 0;
        
        if(style.equals(GameStyle.LOWESTPOINTS)) {
            // winner is user with lowest tempPoints
            winningPoints = currentGamePlayers.get(0).getTempPoints(); // temporarily set this as the lowest point
            for(User aUser: currentGamePlayers) { // find the lowest points first
                if(aUser.getTempPoints() < winningPoints) {
                    winningPoints = aUser.getTempPoints();
                }
            }
            for(User aUser: currentGamePlayers) { // find how many players have same points as winningPoints
                if(aUser.getTempPoints() == winningPoints) {
                    if(noOfWinners > 0) {
                        strWinnersNames = strWinnersNames.concat(", "); // not printed if only one winner
                    }
                    strWinnersNames = strWinnersNames.concat(aUser.getUsername()); // add the winner's name
                    noOfWinners++;
                }                
            }            
        } else if(style.equals(GameStyle.FIRSTTO500)) {
            // winner is user with most points
            winningPoints = currentGamePlayers.get(0).getTempPoints(); // temporarily set this as the highest point
            for(User aUser: currentGamePlayers) { // find the highest points first
                if(aUser.getTempPoints() > winningPoints) {
                    winningPoints = aUser.getTempPoints();
                }
            }
            for(User aUser: currentGamePlayers) { // find how many players have same points as winningPoints
                if(aUser.getTempPoints() == winningPoints) {
                    if(noOfWinners > 0) {
                        strWinnersNames = strWinnersNames.concat(", "); // not printed if only one winner
                    }
                    strWinnersNames = strWinnersNames.concat(aUser.getUsername()); // add the winner's name
                    noOfWinners++;
                }                
            }            
        } else {
            System.out.println("### ERROR: Unknown GameStyle = " + style.toString());
        }
            
        return strWinnersNames;
    }
    
    private String showRoundsPlayed(int roundsPlayed) {
        String rtnString = "";
        rtnString = rtnString.concat("");
        
        rtnString = rtnString.concat("<h3>Rounds Played: " + roundsPlayed + "</h3>");
        
        return rtnString;
    }
    
    private String showGameStyleNote(GameStyle style) {
        String rtnString = "";
        rtnString = rtnString.concat("");
        
        if(style.equals(GameStyle.LOWESTPOINTS)) {
            rtnString = rtnString.concat("<h4><i>Note: Game ends when any player reaches 500 points and the winner is the player with lowest points.</i></h4>");
        } else if(style.equals(GameStyle.FIRSTTO500)) {
            rtnString = rtnString.concat("<h4><i>Note: Game ends when any player reaches 500 points and the player with the highest points is the winner.</i></h4>");
        } else {
            System.out.println("### ERROR: Unknown GameStyle = " + style.toString());
        }
        
        return rtnString;
    }

    // Method to handle GET method request.
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
} // joinGameServlet

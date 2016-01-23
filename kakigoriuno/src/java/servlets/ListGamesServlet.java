package servlets;

import static utilities.Utilities.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Game;
import models.GamesMap;

@WebServlet("/listGames")
public class ListGamesServlet extends HttpServlet {

    @Inject
    GamesMap gamesMap;

    RequestDispatcher rd = null;

    // need the ArrayList, else NullPointerException on add
    public List<Game> gamesList = new ArrayList();
//    public Map<Long, Game> gamesMap = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//        HttpSession session = req.getSession();
        ServletContext appScopeServlet = req.getServletContext();

        int i = 0;
        System.out.println(">>> List of Games in gamesMap:");
        for (Game aGame : gamesMap.getListOfGames()) {
            System.out.println(">>>> Game No. " + i++ + " is " + aGame.toString());
        }

//        if (null == appScopeServlet.getAttribute("gamesList")) {
//            addNewGameToGamesList(req);
//        } else {
//            gamesList = (List<Game>) appScopeServlet.getAttribute("gamesList");
//            String aGameStatus;
//            Boolean listedGameFound = false;
//            for (Game aGame : gamesList) {
//                aGameStatus = aGame.getGameStatus();
//                if (aGameStatus.matches("listed")) {
//                    listedGameFound = true;
//                }
//            }
//            if (!listedGameFound) {
//                addNewGameToGamesList(req);
//            }
//        }
//        if (null == appScopeServlet.getAttribute("gamesMap")) {
//            addNewGameToGamesMap(req);
//        } else {
//            gamesMap = (Map<Long, Game>) appScopeServlet.getAttribute("gamesMap");
//        if (null == appScopeServlet.getAttribute("gamesMap")) {
        // gamesMap not created yet
        // so do all this
        Boolean listedGameFound2 = false;
        Game aGame2 = null;

        // find if there is a listed game inside
        if (gamesMap.isEmpty()) {
            // no games inside
            // make new listed game
            aGame2 = getNewGame();
            System.out.println(">>> 1 aGame2=" + aGame2.toString());
//        } else if (null == gamesMap.findListedGame()) { // dangerous: it seems to remove the listed game
        } else if (null == gamesMap.getFirstGameOfType("listed")) {
            // no listed game
            // make new listed game
            aGame2 = getNewGame();
            System.out.println(">>> 2 aGame2=" + aGame2.toString());
        } else {
            // a listed game found
            listedGameFound2 = true;
            
//            Long listedGameId = gamesMap.findListedGame(); // dangerous: it seems to remove the listed game
//            if (gamesMap.containsKey(listedGameId)) {
//                // gamesMap DOES have the listed game
//                aGame2 = gamesMap.get(listedGameId);
//                System.out.println(">>> 3.1 aGame2=" + aGame2.toString());
//            } else {
//                // somehow this key goes to a game that is null ?!!!
//                aGame2 = getNewGame();
//                System.out.println(">>> 3.2 aGame2=" + aGame2.toString());
//            }

            aGame2 = gamesMap.getFirstGameOfType("listed");

            System.out.println(">>> 3.3 aGame2=" + aGame2.toString());
        }

        // no listed game found, so insert a new listed game into gamesMap
        if (!listedGameFound2) {
            System.out.println(">>> 4 aGame2=" + aGame2.toString());
            addGameToInjectedGamesMap(req, aGame2);
        }

//        }
//        }
        req.getRequestDispatcher("lounge.jsp")
                .forward(req, resp);
//        rd.forward(req, resp);

    } // doPost

    // Method to handle GET method request.
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    private Game getNewGame() {
        Game newGame = new Game();
        
//        Long aLong = newGame.getGameId();
//        newGame.setGameName(Integer.toString(getIntOfSumOfLongDigits(aLong)));

        newGame.listGame();
        return newGame;
    }

    private void addNewGameToGamesList(HttpServletRequest req) {
        Game newGame = getNewGame();
        ServletContext appScopeServlet = req.getServletContext();

        System.out.println(">>> newGame: " + newGame.toString());
        boolean add = gamesList.add(newGame);
        appScopeServlet.setAttribute("gamesList", gamesList);
    }

    private void addNewGameToGamesMap(HttpServletRequest req) {
        Game newGame = getNewGame();
        ServletContext appScopeServlet = req.getServletContext();

        System.out.println(">>> newGame: " + newGame.toString());
        gamesMap.put(newGame.getGameId(), newGame);
        appScopeServlet.setAttribute("gamesMap", gamesMap);
    }

    private void addGameToInjectedGamesMap(HttpServletRequest req, Game newGame) {
        ServletContext appScopeServlet = req.getServletContext();

        System.out.println(">>> newGame: " + newGame.toString());
        gamesMap.put(newGame.getGameId(), newGame);
        appScopeServlet.setAttribute("gamesMap", gamesMap);
    }
} // ListGamesServlet

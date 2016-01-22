package servlets;

import static utilities.Utilities.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import models.GamesMap;
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

        String strGameId, loginUserName, strTableNo;
        HttpSession session = req.getSession();
        ServletContext appScopeServlet = req.getServletContext();
        Long lonGameId = null;

        String strMapGameId, TableNo;

        gamesList = (List<Game>) appScopeServlet.getAttribute("gamesList");
//        gamesMap = (Map<Long, Game>) appScopeServlet.getAttribute("gamesMap");

        User loginUser;
//        User loginUser = new User();
        Game currentGame = new Game();
        Game currentGame2;
        List<User> currentGamePlayers = new ArrayList();
        List<User> currentGamePlayers2 = null;

        loginUser = (User) session.getAttribute("loginuser");
        loginUserName = loginUser.getUsername();
        System.out.println(">>> loginUser=" + loginUser.toString());
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
            System.out.println(">>> from req strMapGameId = " + strMapGameId);
        } else {
            strMapGameId = session.getAttribute("mapGameId").toString();
            System.out.println(">>> from session strMapGameId = " + strMapGameId);
        }
        Long lonMapGameId = Long.valueOf(strMapGameId);
        String strMapTableNo = getIntOfSumOfLongDigits(lonMapGameId).toString();
        session.setAttribute("mapGameId", lonMapGameId);

        TableNo = strMapTableNo;

        currentGame2 = gamesMap.get(lonMapGameId);
        System.out.println(">>> currentGame2 = " + currentGame.toString());

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
        if(!currentGamePlayers2.contains(loginUser)) {
            boolean add = currentGamePlayers2.add(loginUser);            
        }        
        
        String aString = "";
        
        int i = 1;
        for(User aUser: currentGamePlayers2) {
            aString = aString.concat("<tr>\n");
            aString = aString.concat("<td>");
            aString = aString.concat(Integer.toString(i++));
            aString = aString.concat("</td>\n");
            aString = aString.concat("<td>");
            aString = aString.concat(aUser.getUsername());
            aString = aString.concat("</td>\n");
            aString = aString.concat("</tr>\n");
        }

        System.out.println(">>> mapGameId: " + strMapGameId + " NoOfPlayers=" + gamesMap.get(lonMapGameId).getGamePlayers().size());

        // Set refresh, autoload time as 5 seconds
        resp.setIntHeader("Refresh", 5);

        // Set resp content type
        resp.setContentType("text/html");

        // Get current time
        Calendar calendar = new GregorianCalendar();
        String am_pm;
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (calendar.get(Calendar.AM_PM) == 0) {
            am_pm = "AM";
        } else {
            am_pm = "PM";
        }

        String CT = hour + ":" + minute + ":" + second + " " + am_pm;

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
                + "<form method=\"POST\" action=\"joinGame\">\n"
                + "<table border=\"1\">\n"
                + "<tr>\n"
                + "<td>Player Index</td>\n"
                + "<td>Player UserName</td>\n"
                + "</tr>\n"
                + aString
//                + "<tr>\n"
//                + "<td>${gl.getGameName()}</td>\n"
//                + "<td><input type=\"radio\" name=gameId value=\"${gl.getGameId()}\" /> </td>\n"
//                + "</tr>\n"
                + "</table><br><br>\n"
                + "<button type=\"submit\">Join Game</button>\n"
                + "</form>\n"
                + "</body>\n"
                + "</html>\n"
                + "");

        //        req.getRequestDispatcher("clock5secs.jsp")
        //                .forward(req, resp);
    } // doPost

    // Method to handle GET method request.
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
} // ListGamesServlet

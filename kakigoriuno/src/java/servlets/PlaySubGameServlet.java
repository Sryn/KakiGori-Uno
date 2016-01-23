package servlets;

import static utilities.Utilities.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Game;
import models.GamesMap;
import models.User;

@WebServlet("/playSubGame")
public class PlaySubGameServlet extends HttpServlet {

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
//        ServletContext appScopeServlet = req.getServletContext();
        Long lonMapGameId;
        int i, currGameNoOfPlayers;

        User loginUser;
        Game currentGame;

        String strMapGameId, strGameName, loginUserName;
        String strRoundNo = "X";

        loginUser = (User) session.getAttribute("loginuser");
        loginUserName = loginUser.getUsername();

//        strMapGameId = req.getParameter("mapGameId");
        strMapGameId = (String) session.getAttribute("mapGameId");
        System.out.println("> loginUser=" + loginUserName + " sesAtt mapGameId=" + strMapGameId);

        lonMapGameId = Long.valueOf(strMapGameId);
        currentGame = gamesMap.get(lonMapGameId);
        strGameName = currentGame.getGameName();
//        currGameNoOfPlayers = currentGame.getGamePlayers().size();
        currGameNoOfPlayers = 10; // temporary only

        String strHttpPlayersRow = "";
        strHttpPlayersRow = strHttpPlayersRow.concat("<tr>\n");
        for (i = 1; i <= currGameNoOfPlayers; i++) {
            if(i == 6) {
                strHttpPlayersRow = strHttpPlayersRow.concat("</tr>\n<tr>\n");        
            }
            strHttpPlayersRow = strHttpPlayersRow.concat("<td><table border=\"1\">\n<tr><td align=\"center\">");
            strHttpPlayersRow = strHttpPlayersRow.concat("<img src=\"images\\avatars\\Avatar_" + getRandomInt(1, 16) + ".png\" width=\"100\" height=\"100\" alt=\"Player " + i + " Avatar\">");
            strHttpPlayersRow = strHttpPlayersRow.concat("</td></tr><tr><td>");
            strHttpPlayersRow = strHttpPlayersRow.concat("dirArrow + userName + handCount");
            strHttpPlayersRow = strHttpPlayersRow.concat("</td></tr></table>\n</td>\n");
        }
        strHttpPlayersRow = strHttpPlayersRow.concat("</tr>\n");

        String playOrDrawCardBtnVisibility = "";
        // show button when its the loginUser's turn
        if (true) {
            playOrDrawCardBtnVisibility = playOrDrawCardBtnVisibility.concat("<button type=\"submit\" name=mapGameId value=\"");
            playOrDrawCardBtnVisibility = playOrDrawCardBtnVisibility.concat(strMapGameId);
            playOrDrawCardBtnVisibility = playOrDrawCardBtnVisibility.concat("\">Play or Draw Card</button>\n");
        }

        // Set refresh, autoload time as 5 seconds
        resp.setHeader("Refresh", "5");

        // Set resp content type
        resp.setContentType("text/html");

        String CT = getCurrentTimeString();

        PrintWriter out = resp.getWriter();
        String pageTitle = "Uno Game " + strGameName;
//        String bodyTitle = "Welcome to Table " + TableNo + " " + loginUserName + "!";
        String bodyTitle = loginUserName + " @ Game " + strGameName + " Round " + strRoundNo;
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
                //                + "<a href=\"listGames\">Return to Games Lounge</a><br><br>\n"
                + "<h3>Move No.&nbsp" + "roundMoveNo" + "&nbspfor Player&nbsp" + "turnPlayerName" + "</h3><br>"
                + "<form method=\"POST\" action=\"processMove\">\n"
                + "<table border=\"1\">\n"
                // Player Avatars row(s)
                + strHttpPlayersRow
                // draw and discard row
                + "<tr>\n"
                // draw pile cell
                + "<td colspan=\"2\">"
                + "<table border=\"1\">\n<tr><td>"
                + "Draw Pile Picture"
                + "</td></tr><tr><td>"
                + "drawPileCardCount + RadioButton"
                + "</td></tr></table>\n"
                + "</td>\n"
                + "<td>Blank Cell</td>\n"
                // discard pile cell
                + "<td colspan=\"2\">"
                + "<table border=\"1\">\n<tr><td>"
                + "Discard Pile Picture"
                + "</td></tr><tr><td>"
                + "discardPileCardCount"
                + "</td></tr></table>\n"
                + "</td>\n"
                + "</tr>\n"
                // loginPlayer hand row 1
                + "<tr>\n"
                // card 1
                + "<td><table border=\"1\">\n<tr><td>"
                + "loginPlayer Hand Card 1 picture"
                + "</td></tr><tr><td>"
                + "cardName(?) + RadioButton"
                + "</td></tr></table>\n</td>\n"
                // card 2
                + "<td><table border=\"1\">\n<tr><td>"
                + "loginPlayer Hand Card 2 picture"
                + "</td></tr><tr><td>"
                + "cardName(?) + RadioButton"
                + "</td></tr></table>\n</td>\n"
                // card 3
                + "<td><table border=\"1\">\n<tr><td>"
                + "loginPlayer Hand Card 3 picture"
                + "</td></tr><tr><td>"
                + "cardName(?) + RadioButton"
                + "</td></tr></table>\n</td>\n"
                // card 4
                + "<td><table border=\"1\">\n<tr><td>"
                + "loginPlayer Hand Card 4 picture"
                + "</td></tr><tr><td>"
                + "cardName(?) + RadioButton"
                + "</td></tr></table>\n</td>\n"
                // card 5
                + "<td><table border=\"1\">\n<tr><td>"
                + "loginPlayer Hand Card 5 picture"
                + "</td></tr><tr><td>"
                + "cardName(?) + RadioButton"
                + "</td></tr></table>\n</td>\n"
                + "</tr>\n"
                // loginPlayer hand row 2
                + "<tr>\n"
                // card 6
                + "<td><table border=\"1\">\n<tr><td>"
                + "loginPlayer Hand Card 6 picture"
                + "</td></tr><tr><td>"
                + "cardName(?) + RadioButton"
                + "</td></tr></table>\n</td>\n"
                // card 7
                + "<td><table border=\"1\">\n<tr><td>"
                + "loginPlayer Hand Card 7 picture"
                + "</td></tr><tr><td>"
                + "cardName(?) + RadioButton"
                + "</td></tr></table>\n</td>\n"
                + "</tr>\n"
                + "</table><br><br>\n"
                + playOrDrawCardBtnVisibility
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
} // playSubGamesServlet

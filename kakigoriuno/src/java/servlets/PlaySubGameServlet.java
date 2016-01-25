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
import models.*;
import models.CardList.CardListType;
import models.SubGame.Direction;
//import models.CardList;
//import models.Game;
//import models.GamesMap;
//import models.Player;
//import models.SubGame;
//import models.User;

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
        int i, currSubGameNoOfPlayers = 0, roundNo = 0, roundMoveNo = 1;
        Boolean loadError = true;

        User loginUser;
        Game currentGame;
        SubGame currentSubGame;
        Player turnPlayer = null;

        String strMapGameId, strGameName, loginUserName;
        String strRoundNo = "X";

        List<Player> currentSubGamePlayers;

        loginUser = (User) session.getAttribute("loginuser");
        loginUserName = loginUser.getUsername();

        strMapGameId = (String) session.getAttribute("mapGameId");
        System.out.println("> loginUser=" + loginUserName + " sesAtt mapGameId=" + strMapGameId);

        lonMapGameId = Long.valueOf(strMapGameId);
        currentGame = gamesMap.get(lonMapGameId);
        strGameName = currentGame.getGameName();

        currentSubGame = currentGame.getCurrentSubGame();
        
        if(null == currentSubGame) {
            // return back to StartGameServLet
            loadError = false;
            resp.setHeader("Refresh", "0; startGame");
        }

        if(null == currentSubGame.getSubGamePlayers()) {
            loadError = false;
            resp.setHeader("Refresh", "0; startGame");
        } else {
            currentSubGamePlayers = currentSubGame.getSubGamePlayers();
            currSubGameNoOfPlayers = currentSubGamePlayers.size();
            roundNo = currentSubGame.getRoundNo();
            turnPlayer = currentSubGame.getCurrentPlayer();
        }
        
        // Set refresh, autoload time as 5 seconds
        resp.setHeader("Refresh", "5");

        // Set resp content type
        resp.setContentType("text/html");

        String CT = getCurrentTimeString();

        PrintWriter out = resp.getWriter();
        String pageTitle = "Uno Game " + strGameName;
//        String bodyTitle = "Welcome to Table " + TableNo + " " + loginUserName + "!";
        String bodyTitle = loginUserName + " @ Game " + strGameName + " Round " + roundNo;
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
                + "<h3>Move No.&nbsp" + roundMoveNo + "&nbspfor Player&nbsp" + turnPlayer.getPlayer().getUsername() + "</h3><br>"
                + "<form method=\"POST\" action=\"processMove\">\n"
                + "<table border=\"1\">\n"
                // Player Avatars row(s)
//                + getPlayersRow(currSubGameNoOfPlayers)
                + getPlayersRow(currentSubGame)
                // draw and discard row
                + "<tr>\n"
                // draw pile cell
                + getDrawPileCell()
                // empty cell
                + "<td>&nbsp</td>\n"
                // discard pile cell
                + getDiscardPileCell()
                + "</tr>\n"
                // loginPlayer hand row 1
                + getHandRow()
                + "</table><br><br>\n"
                + getPlayOrDrawCardBtn(strMapGameId)
                //                + playOrDrawCardBtnVisibility
                + "</form>\n"
                + "</body>\n"
                + "</html>\n"
                + "");

        //        req.getRequestDispatcher("clock5secs.jsp")
        //                .forward(req, resp);
    } // doPost

    public String getPlayOrDrawCardBtn(String strMapGameId) {
        String playOrDrawCardBtnVisibility = "";
        // show button when its the loginUser's turn
        if (true) {
            playOrDrawCardBtnVisibility = playOrDrawCardBtnVisibility.concat("<button type=\"submit\" name=mapGameId value=\"");
            playOrDrawCardBtnVisibility = playOrDrawCardBtnVisibility.concat(strMapGameId);
            playOrDrawCardBtnVisibility = playOrDrawCardBtnVisibility.concat("\">Play or Draw Card</button>\n");
        }
        return playOrDrawCardBtnVisibility;
    }

    public String getHandRow() {
        int i, intRandHandCardCount = getRandomInt(1, 15);
        String strHttpHandRow = "";
        strHttpHandRow = strHttpHandRow.concat("<tr>\n");
        for (i = 1; i <= intRandHandCardCount; i++) {
            if ((i != 1) && (i % 5 == 1)) {
                strHttpHandRow = strHttpHandRow.concat("</tr>\n<tr>\n");
            }
            strHttpHandRow = strHttpHandRow.concat("<td><table border=\"1\">\n<tr><td align=\"center\">");
            strHttpHandRow = strHttpHandRow.concat("<img src=\"images\\uno_deck\\" + getRandomUnoCardFileName() + "\" width=\"85\" height=\"128\" alt=\"Player's Hand Card No. " + i + " Face\">");
            strHttpHandRow = strHttpHandRow.concat("</td></tr><tr><td>");
            strHttpHandRow = strHttpHandRow.concat("cardName(?) + RadioButton");
            strHttpHandRow = strHttpHandRow.concat("</td></tr></table>\n</td>\n");
        }
        strHttpHandRow = strHttpHandRow.concat("</tr>\n");
        return strHttpHandRow;
    }

    public String getDiscardPileCell() {
        String strHttpDiscardPileCell = "";
        strHttpDiscardPileCell = strHttpDiscardPileCell.concat("<td colspan=\"2\" align=\"center\">");
        strHttpDiscardPileCell = strHttpDiscardPileCell.concat("<table border=\"1\">\n<tr><td align=\"center\">");
        strHttpDiscardPileCell = strHttpDiscardPileCell.concat("<img src=\"images\\uno_deck\\" + getRandomUnoCardFileName() + "\" alt=\"Discard Pile Card Face\">");
        strHttpDiscardPileCell = strHttpDiscardPileCell.concat("</td></tr><tr><td>");
        strHttpDiscardPileCell = strHttpDiscardPileCell.concat("drawPileCardCount + RadioButton");
        strHttpDiscardPileCell = strHttpDiscardPileCell.concat("</td></tr></table>\n");
        strHttpDiscardPileCell = strHttpDiscardPileCell.concat("</td>\n");
        return strHttpDiscardPileCell;
    }

    public String getDrawPileCell() {
        String strHttpDrawPileCell = "";
        strHttpDrawPileCell = strHttpDrawPileCell.concat("<td colspan=\"2\" align=\"center\">");
        strHttpDrawPileCell = strHttpDrawPileCell.concat("<table border=\"1\">\n<tr><td align=\"center\">");
        strHttpDrawPileCell = strHttpDrawPileCell.concat("<img src=\"images\\uno_deck\\back.png\" alt=\"Draw Pile Card Back\">");
        strHttpDrawPileCell = strHttpDrawPileCell.concat("</td></tr><tr><td>");
        strHttpDrawPileCell = strHttpDrawPileCell.concat("drawPileCardCount + RadioButton");
        strHttpDrawPileCell = strHttpDrawPileCell.concat("</td></tr></table>\n");
        strHttpDrawPileCell = strHttpDrawPileCell.concat("</td>\n");
        return strHttpDrawPileCell;
    }

//    public String getPlayersRow(int currGameNoOfPlayers) {
    public String getPlayersRow(SubGame currentSubGame) {
        int i, currGameNoOfPlayers, currentUserIdx;
        String strHttpPlayersRow = "", ACW_Arrow = "<- ", CW_Arrow = " ->";
        strHttpPlayersRow = strHttpPlayersRow.concat("<tr>\n");
        
        if(currentSubGame.getLastDirection().equals(Direction.CLOCKWISE)) {
            // clockwise
            ACW_Arrow = ""; // clear this so it won't be printed
        } else {
            // anti-clockwise
            CW_Arrow = ""; // clear this so it won't be printed
        }
        
        currentSubGame.getSubGamePlayers().indexOf(currentSubGame.getCurrentPlayer());
        
        currentUserIdx = currGameNoOfPlayers = currentSubGame.getSubGamePlayers().size();
        
        for (i = 0; i < currGameNoOfPlayers; i++) {
            if (i == 5) {
                strHttpPlayersRow = strHttpPlayersRow.concat("</tr>\n<tr>\n");
            }
            strHttpPlayersRow = strHttpPlayersRow.concat("<td><table border=\"1\">\n<tr><td align=\"center\">");
            strHttpPlayersRow = strHttpPlayersRow.concat("<img src=\"images\\avatars\\Avatar_" + getRandomInt(1, 16) + ".png\" width=\"100\" height=\"100\" alt=\"Player " + i + " Avatar\">");
            strHttpPlayersRow = strHttpPlayersRow.concat("</td></tr><tr><td>");
            if(i == currentUserIdx)
                strHttpPlayersRow = strHttpPlayersRow.concat(ACW_Arrow); // Anti-Clockwise Arrow
//            strHttpPlayersRow = strHttpPlayersRow.concat("userName - ");
            strHttpPlayersRow = strHttpPlayersRow.concat(currentSubGame.getSubGamePlayers().get(i).getPlayer().getUsername() + " - ");
//            strHttpPlayersRow = strHttpPlayersRow.concat("handCount");
            strHttpPlayersRow = strHttpPlayersRow.concat(Integer.toString(currentSubGame.getSubGamePlayers().get(i).getHand().size()) + " ");
            if(i == currentUserIdx)
                strHttpPlayersRow = strHttpPlayersRow.concat(CW_Arrow); // Clockwise Arrow
            strHttpPlayersRow = strHttpPlayersRow.concat("</td></tr></table>\n</td>\n");
        }
        strHttpPlayersRow = strHttpPlayersRow.concat("</tr>\n");
        return strHttpPlayersRow;
    }

    // Method to handle GET method request.
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
} // playSubGamesServlet

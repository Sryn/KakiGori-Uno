<%-- 
    Document   : lounge
    Created on : Jan 19, 2016, 5:17:04 PM
    Author     : Sryn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>KakiGori Uno Lounge</title>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%
            // Set refresh, autoload time as 5 seconds
//            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "listGames");
            response.setIntHeader("Refresh", 5);
        %>
    </head>
    <body>
        <h2>Welcome <c:out value="${sessionScope.loginuser.getUsername()}"/>!</h2>
<!--
        <form method="POST" action="joinGame">
            <table border="1">
                <tr>
                    <td>Table Name</td>
                    <td>Status</td>
                    <td>No. of Players</td>
                    <td>Choose</td>
                </tr>
                <c:forEach items="${applicationScope.gamesList}" var="gl" varStatus="i">
                    <tr>
                        <td>${gl.getGameName()}</td>
                        <td>${gl.getGameStatus()}</td>
                        <td>
                            <c:if test="${empty gl.getGamePlayers().size()}">
                                0
                            </c:if>
                            <c:if test="${not empty gl.getGamePlayers().size()}">
                                ${gl.getGamePlayers().size()}
                            </c:if>
                        </td>
                        <td><input type="radio" name=gameId value="${gl.getGameId()}"/></td>
                    </tr>
                </c:forEach>
            </table>
            <button type="submit">Join Table</button>
        </form>
-->
        <hr>
        <h3>Number of Games: ${gamesmap.size()}</h3>
        <form method="POST" action="joinGame">
            <table border="1">
                <tr>
                    <td>Table Name</td>
                    <td>Status</td>
                    <td>No. of Players</td>
                    <td>Choose</td>
                </tr>
                <c:forEach items="${gamesmap.listOfGames}" var="game" varStatus="i">
                    <tr>
                        <td>${game.getGameName()}</td>
                        <td>${game.getGameStatus()}</td>
                        <td>
                            <c:if test="${empty game.getGamePlayers().size()}">
                                0
                            </c:if>
                            <c:if test="${not empty game.getGamePlayers().size()}">
                                ${game.getGamePlayers().size()}
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${empty game.getGamePlayers().size()}">
                                <input type="radio" name=mapGameId value="${game.getGameId()}"/>
                            </c:if>
                            <c:if test="${game.getGamePlayers().size() <= 10}">
                                <input type="radio" name=mapGameId value="${game.getGameId()}"/>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br>
            <button type="submit">Join Table</button>
        </form>

    </body>
</html>

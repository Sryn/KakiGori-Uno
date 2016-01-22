<%-- 
    Document   : index
    Created on : Jan 19, 2016, 3:22:56 PM
    Author     : Sryn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>KakiGori Uno</title>
    </head>
    <body>
        <h1>Welcome to KakiGori Uno</h1>
        <h2>${message}</h2>
        <form method="POST" action="verifyLogin">
            <table>
                <tr>
                    <td>Username:</td>
                    <td>
                        <input type="text" size="30" name="username"/>
                    <td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td>
                        <input type="password" size="30" name="password"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <button type="submit">Login</button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

//import javax.servlet.http.HttpServlet;

/**
 *
 * @author Sryn
 */
//public abstract class Utilities extends HttpServlet {
public final class Utilities {
    
    public static Integer getIntOfSumOfLongDigits(Long aLong) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String strLong = aLong.toString();
        int anInt = 0;

        for (int i = 0; i < strLong.length(); i++) {
            anInt += Character.getNumericValue(strLong.charAt(i));
        }

        return (anInt);
    }

}

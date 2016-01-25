/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

//import javax.servlet.http.HttpServlet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

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

    public static String getCurrentTimeString() {
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
        String strMinute = "";
        String strSecond = "";
        if (minute < 10) {
            strMinute = strMinute.concat("0");
            strMinute = strMinute.concat(Integer.toString(minute));
        } else {
            strMinute = Integer.toString(minute);
        }
        if (second < 10) {
            strSecond = strSecond.concat("0");
            strSecond = strSecond.concat(Integer.toString(second));
        } else {
            strSecond = Integer.toString(second);
        }
        String CT = hour + ":" + strMinute + ":" + strSecond + " " + am_pm;
        return CT;
    }

    public static Long getRandomLong() {
        Random randomno = new Random();
        long value = randomno.nextLong();
        return value;
    }

    public static int getRandomInt() {
        Random randomno = new Random();
        int value = randomno.nextInt();
        return value;
    }

    public static int getRandomInt(int exclusiveUpperBound) {
        Random randomno = new Random();
        int value = randomno.nextInt(exclusiveUpperBound);
        return value;
    }

    public static int getRandomInt(int inclusiveLowerBound, int inclusiveUpperBound) {
        int range = inclusiveUpperBound - inclusiveLowerBound + 1;
        int value = getRandomInt(range);
        return (inclusiveLowerBound + value);
    }

    public static String getRandomUnoCardFileName() {
        String cardFileName = "";
        int value, colour;

        colour = getRandomInt(0, 4); // 0: Red, 1: Yellow, 2: Green, 3: Blue, 4: Black

        if (colour == 4) {
            value = getRandomInt(0, 1); // 0: Wild, 1: Wild + Draw 4
        } else {
            value = getRandomInt(0, 12); // 0-9: Card Value, 10: Skip, 11: Reverse, 12: Draw 2
        }

        cardFileName = cardFileName.concat("c");
        cardFileName = cardFileName.concat(Integer.toString(colour));

        if (value < 10) {
            cardFileName = cardFileName.concat("_0");
        } else {
            cardFileName = cardFileName.concat("_");
        }

        cardFileName = cardFileName.concat(Integer.toString(value));
        cardFileName = cardFileName.concat(".png");

        return cardFileName;
    }
}

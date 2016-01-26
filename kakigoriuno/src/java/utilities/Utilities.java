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
import models.Card;

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
    
    public static boolean pairOfCardMatchDeterminator(Card card1, Card card2) {
        
        Boolean matchColour, matchAction, matchValue = false;
        
        Card.Colour card1Colour, card2Colour;
        Card.Action card1Action, card2Action;
        Integer card1Value, card2Value;
        
        card1Colour = card1.getCardColour();
        card2Colour = card2.getCardColour();
        
        card1Action = card1.getCardAction();
        card2Action = card2.getCardAction();
        
        card1Value = card1.getCardValue();
        card2Value = card2.getCardValue();
        
        if(card1Colour.equals(Card.Colour.BLACK) || card2Colour.equals(Card.Colour.BLACK))
            matchColour = true;
        else if(card1Colour.equals(card2Colour))
            matchColour = true;
        else
            matchColour = false;
        
        if(card1Action.equals(Card.Action.WILD) || card1Action.equals(Card.Action.WILD_DRAW4))
            matchAction = true;
        else if(card2Action.equals(Card.Action.WILD) || card2Action.equals(Card.Action.WILD_DRAW4))
            matchAction = true;
        else if(card1Action.equals(Card.Action.NUMBER) || card2Action.equals(Card.Action.NUMBER))
            matchAction = false;
        else if(card1Action.equals(card2Action))
            matchAction = true;
        else
            matchAction = false;
        
        if(card1Action.equals(Card.Action.NUMBER) && card2Action.equals(Card.Action.NUMBER))
            if(card1Value.equals(card2Value))
                matchValue = true;
        else
                matchValue = false;
        
        return (matchColour || matchAction || matchValue); // returns true is any one is true
    }
    
}

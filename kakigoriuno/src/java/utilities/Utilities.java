/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

//import javax.servlet.http.HttpServlet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import models.Card;
import models.Card.Colour;
import models.CardList;

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
    
    public static boolean pairOfCardMatchDeterminator(Card playerCard, CardList discardPile, Colour validColour) {
        
        Card discardTopCard = discardPile.getTopCard();
        
        Boolean matchColour, matchAction, matchValue = false;
        
        Card.Colour playerCardColour;
        Card.Action playerCardAction, discardTopCardAction;
        Integer playerCardValue, discardTopCardValue;
        
        playerCardColour = playerCard.getCardColour();
//        validColour = discardTopCard.getCardColour();
        
        playerCardAction = playerCard.getCardAction();
        discardTopCardAction = discardTopCard.getCardAction();
        
        playerCardValue = playerCard.getCardValue();
        discardTopCardValue = discardTopCard.getCardValue();
        
        if(playerCardColour.equals(Card.Colour.BLACK))
            matchColour = true;
        else if(playerCardColour.equals(validColour))
            matchColour = true;
        else
            matchColour = false;
        
        if(playerCardAction.equals(Card.Action.WILD) || playerCardAction.equals(Card.Action.WILD_DRAW4))
            matchAction = true;
//        else if(discardTopCardAction.equals(Card.Action.WILD) || discardTopCardAction.equals(Card.Action.WILD_DRAW4))
//            matchAction = true;
        else if(playerCardAction.equals(Card.Action.NUMBER) || discardTopCardAction.equals(Card.Action.NUMBER))
            matchAction = false;
        else if(playerCardAction.equals(discardTopCardAction))
            matchAction = true;
        else
            matchAction = false;
        
        if(playerCardAction.equals(Card.Action.NUMBER) && discardTopCardAction.equals(Card.Action.NUMBER))
            if(playerCardValue.equals(discardTopCardValue))
                matchValue = true;
        else
                matchValue = false;
        
        return (matchColour || matchAction || matchValue); // returns true is any one is true
    }
    
}

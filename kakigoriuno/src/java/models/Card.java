/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import static java.util.Objects.isNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import static utilities.Utilities.*;

/**
 *
 * @author Sryn
 */
@Entity
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cardId;

    private String cardName;
    private Colour cardColour;
    private Action cardAction;
    private Integer cardValue;
    private int cardPoints;
    private String imgFileName;

    public enum Colour {
        RED,
        YELLOW,
        GREEN,
        BLUE,
        BLACK
    }

    public enum Action {
        NUMBER,
        SKIP,
        REVERSE,
        DRAW2,
        WILD,
        WILD_DRAW4
    }

    public Card(int i) {
//        super();
        this.cardId = getRandomLong();
        this.cardColour = getCardColour(i);
        this.cardAction = getCardAction(i);
        this.cardValue = getCardValue(i);
        this.cardPoints = getCardPoints(i);
        this.imgFileName = getImgFileName(i);
        this.cardName = getCardName(i);
    }

    public Card(String cardName, int cardPoints) {
        this.cardName = cardName;
        this.cardPoints = cardPoints;
    }

    private Colour getCardColour(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.        
        Colour privCardColour = null;

        if ((i >= 1) && (i <= 25)) {
            privCardColour = Colour.RED;
        } else if ((i >= 26) && (i <= 50)) {
            privCardColour = Colour.YELLOW;
        } else if ((i >= 51) && (i <= 75)) {
            privCardColour = Colour.GREEN;
        } else if ((i >= 76) && (i <= 100)) {
            privCardColour = Colour.BLUE;
        } else if ((i >= 101) && (i <= 108)) {
            privCardColour = Colour.BLACK;
        }

        return privCardColour;
    }

    private Action getCardAction(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Action privCardType = Action.NUMBER;

        if ((i >= 101) && (i <= 104)) {
            privCardType = Action.WILD;
        } else if ((i >= 105) && (i <= 108)) {
            privCardType = Action.WILD_DRAW4;
        } else if (((i % 25) == 20) || ((i % 25) == 21)) {
            privCardType = Action.SKIP;
        } else if (((i % 25) == 22) || ((i % 25) == 23)) {
            privCardType = Action.REVERSE;
        } else if (((i % 25) == 24) || ((i % 25) == 0)) {
            privCardType = Action.DRAW2;
        }

        return privCardType;
    }

    private Integer getCardValue(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Integer privCardValue = null;

        if ((i >= 1) && (i <= 100)) {
            if (((i % 25) >= 1) && ((i % 25) <= 19)) {
                privCardValue = getIntFromIMod25DivBy2(i);
            }
        }

        return privCardValue;
    }

    private int getCardPoints(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int privCardPoints = 0;

        if ((i >= 101) && (i <= 108)) {
            privCardPoints = 50;
        } else if ((((i % 25) >= 20) && ((i % 25) <= 24)) || ((i % 25) == 0)) {
            privCardPoints = 20;
        } else {
            privCardPoints = getIntFromIMod25DivBy2(i);
        }

        return privCardPoints;
    }

    private int getIntFromIMod25DivBy2(int i) {
        int anInt;
        Double d = Math.floor((i % 25) / 2);
        anInt = d.intValue();
        return anInt;
    }

    private String getImgFileName(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String fileName = "";

        fileName = fileName.concat("c");
        fileName = fileName.concat(getStrIntOfCardColour(i));
        fileName = fileName.concat("_");
        fileName = fileName.concat(getStrIntOfCardValue(i));
        fileName = fileName.concat(".png");

        return fileName;
    }

    private String getStrIntOfCardColour(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String strIntOfCardColour;
        Colour switchColour = getCardColour(i);

        switch (switchColour) {
            case RED:
                strIntOfCardColour = "0";
                break;
            case YELLOW:
                strIntOfCardColour = "1";
                break;
            case GREEN:
                strIntOfCardColour = "2";
                break;
            case BLUE:
                strIntOfCardColour = "3";
                break;
            case BLACK:
                strIntOfCardColour = "4";
                break;
            default:
                strIntOfCardColour = null;
                break; // error
        }

        return strIntOfCardColour;
    }

    private String getStrIntOfCardValue(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String strIntOfCardValue;
        Integer privCardValue = getCardValue(i);

        if (isNull(privCardValue)) {
            // action card
            Action switchAction = getCardAction(i);

            switch (switchAction) {
                case SKIP:
                    strIntOfCardValue = "10";
                    break;
                case REVERSE:
                    strIntOfCardValue = "11";
                    break;
                case DRAW2:
                    strIntOfCardValue = "12";
                    break;
                case WILD:
                    strIntOfCardValue = "00";
                    break;
                case WILD_DRAW4:
                    strIntOfCardValue = "01";
                    break;
                default:
                    System.out.println("### Error: number card in getStrIntOfCardValue(" + i + ")");
                    strIntOfCardValue = "";
                    break; // error: number card
            }
        } else {
            // number card
            strIntOfCardValue = "0";
            strIntOfCardValue = strIntOfCardValue.concat(privCardValue.toString());
        }
        return strIntOfCardValue;
    }

    private String getCardName(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String strCardColour, strActionOrNumber;

        // get colour name
        strCardColour = getCardColour(i).toString().toLowerCase(); // Colour.values()[i].toString().toLowerCase();

        // don't call it BLACK WIlD etc
        if (strCardColour.equals(Colour.BLACK.toString().toLowerCase())) {
            strCardColour = "";
        } else {
            // else capitalise first letter of colour
            strCardColour = capitaliseFirstLetter(strCardColour);
            strCardColour = strCardColour.concat(" "); // give a space after
        }

        // get action name
        strActionOrNumber = getCardAction(i).toString().toLowerCase(); // Action.values()[i].toString().toLowerCase();

        if (strActionOrNumber.equals(Action.NUMBER.toString().toLowerCase())) {
            // a number
            strActionOrNumber = getCardValue(i).toString();
        } else // if WILD_DRAW4
        if (getCardAction(i).equals(Action.WILD_DRAW4)) {
            strActionOrNumber = "Wild + Draw4";
        } else {
            // else capitalise first letter of action
            strActionOrNumber = capitaliseFirstLetter(strActionOrNumber);
        }

        return (strCardColour.concat(strActionOrNumber));
    }

    private String capitaliseFirstLetter(String aString) {
        String properString = aString.substring(0, 1);
        properString = properString.toUpperCase();
        properString = properString.concat(aString.substring(1));

        return properString;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Colour getCardColour() {
        return cardColour;
    }

    public void setCardColour(Colour cardColour) {
        this.cardColour = cardColour;
    }

    public Action getCardAction() {
        return cardAction;
    }

    public void setCardAction(Action cardAction) {
        this.cardAction = cardAction;
    }

    public Integer getCardValue() {
        return cardValue;
    }

    public void setCardValue(Integer cardValue) {
        this.cardValue = cardValue;
    }

    public Integer getCardPoints() {
        return cardPoints;
    }

    public void setCardPoints(Integer cardPoints) {
        this.cardPoints = cardPoints;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardId != null ? cardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Card)) {
            return false;
        }
        Card other = (Card) object;
        if ((this.cardId == null && other.cardId != null) || (this.cardId != null && !this.cardId.equals(other.cardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.card[ id=" + cardId
                + ", Name=" + cardName
                + ", Colour=" + this.getCardColour().toString()
                + ", Action=" + this.getCardAction().toString()
                + ", Value=" + this.getCardValue()
                + ", Points=" + this.getCardPoints().toString()
                + ", File=" + imgFileName
                + " ]\n";
    }

}

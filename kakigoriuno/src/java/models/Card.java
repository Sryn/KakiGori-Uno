/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String cardColour;
    private String cardValue;
    private Integer cardPoints;

    public Card() {
        super();
    }
    
    public Card(String cardName, String cardColour, String cardValue, Integer cardPoints) {
        this.cardName = cardName;
        this.cardColour = cardColour;
        this.cardValue = cardValue;
        this.cardPoints = cardPoints;
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

    public String getCardColour() {
        return cardColour;
    }

    public void setCardColour(String cardColour) {
        this.cardColour = cardColour;
    }

    public String getCardValue() {
        return cardValue;
    }

    public void setCardValue(String cardValue) {
        this.cardValue = cardValue;
    }

    public Integer getCardPoints() {
        return cardPoints;
    }

    public void setCardPoints(Integer cardPoints) {
        this.cardPoints = cardPoints;
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
        return "com.acme.sa41.day1.web.card[ cardid=" + cardId + ", cardName=" + cardName + " ]";
    }
    
}

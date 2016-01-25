/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.OneToMany;
import static utilities.Utilities.*;

/**
 *
 * @author Sryn
 */
@Entity
public class CardList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cardListId;

    private CardListType listType; // drawPile, discardPile, playerHand
    
    public enum CardListType{
        DRAWPILE,
        DISCARDPILE,
        PLAYERHAND
    }
    
    @OneToMany
    private List<Card> listOfCards;

    // constructor
    public CardList(CardListType listType) {
        this.cardListId = getRandomLong();
        this.listType = listType;
        
        this.listOfCards = new ArrayList();
        
        if(listType.equals(CardListType.DRAWPILE)) {
            // initialise listOfCards with a new shuffled deck of 108 cards
            for(int i=1; i<=108; i++) {
                Card aCard = new Card(i);
                this.listOfCards.add(aCard);
            }
            
            // temporarily don't shuffle
            //this.shuffleCards();            
        }
            
    }

    public Long getCardListId() {
        return cardListId;
    }

    public void setCardListId(Long cardListId) {
        this.cardListId = cardListId;
    }

    public CardListType getListType() {
        return listType;
    }

    public void setListType(CardListType listType) {
        this.listType = listType;
    }

    public List<Card> getListOfCards() {
        return listOfCards;
    }

    public void setListOfCards(List<Card> listOfCards) {
        this.listOfCards = listOfCards;
    }

    public Boolean addCard(Card card) {
        return this.listOfCards.add(card);
    }

    public Boolean removeCard(Card card) {
        return this.listOfCards.remove(card);
    }

    // gets and removes the first card
    public Card drawCard() {
        return this.listOfCards.remove(0);
    }

    public void shuffleCards() {

        Card tempCard;
        Integer randomInt;

        // swapping two cards, A and B, at a time
        // with the Card A being chosen succeedingly throughout the list from the first card till last
        // with the Card B being a randomly chosen card
        // note that a card may be swapped more than once
        // and a card may actually be finally swapped right back into its original position
        for (int i = 0; i < this.listOfCards.size(); i++) {
            tempCard = this.listOfCards.remove(i); // remove Card A
            randomInt = ThreadLocalRandom.current().nextInt(0, this.listOfCards.size()); // choose a random index for Card B
            this.listOfCards.add(i, this.listOfCards.get(randomInt)); // put a copy of Card B in A's previous position
            this.listOfCards.remove(randomInt + 1); // remove original card B, since the card has shifted right by 1
            this.listOfCards.add(randomInt + 1, tempCard); // place back Card A into B's original position, need the +1 else it'll be a three cards swap
        }
    }
    
    public int size() {
        return (this.listOfCards.size());
    }
    
    public Card getTopCard() {
        if(this.listOfCards.isEmpty())
            return null;
        else
            return this.listOfCards.get(0);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardListId != null ? cardListId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CardList)) {
            return false;
        }
        CardList other = (CardList) object;
        if ((this.cardListId == null && other.cardListId != null) || (this.cardListId != null && !this.cardListId.equals(other.cardListId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String rtnString;
                
        rtnString = "models.CardList[ id=" + cardListId + " ]\n";
        for(Card aCard: this.getListOfCards()) {
            rtnString = rtnString.concat(aCard.toString());
        }
        
        return rtnString;
    }

}

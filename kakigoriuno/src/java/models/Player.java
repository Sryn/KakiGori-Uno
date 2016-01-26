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
import javax.persistence.OneToOne;
import models.CardList.CardListType;
import static utilities.Utilities.*;

/**
 *
 * @author Sryn
 */
@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long playerId;
    
    private User player;
    
    @OneToOne
    private CardList hand;
    
    private int gamePoints;
    private PlayerStatus playerStatus; // active, inactive
    
    public enum PlayerStatus{
        ACTIVE,
        INACTIVE
    }

    // constructor
    public Player(User player) {
        this.playerId = getRandomLong();
        this.player = player;
        this.hand = new CardList(CardListType.PLAYERHAND);
        this.gamePoints = 0;
        this.setPlayerStatus(PlayerStatus.ACTIVE);
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
    
    public User getPlayer() {
        return player;
    }
    
    public void setPlayer(User player) {
        this.player = player;
    }
    
    public CardList getHand() {
        return hand;
    }
    
    public void setHand(CardList hand) {
        this.hand = hand;
        this.hand.setListType(CardListType.PLAYERHAND);
    }

    public Integer getGamePoints() {
        return gamePoints;
    }

    public void setGamePoints(Integer gamePoints) {
        this.gamePoints = gamePoints;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }
    
    public int countHowManyMatchingCards(Card currentDiscardPileTopCard) {
        int count = 0;
        
        for(Card aCard: this.hand.getListOfCards()) {
            if(pairOfCardMatchDeterminator(aCard, currentDiscardPileTopCard))
                count++;
        }
        
        return count;
    }
    
    public int addUpHandPoints() {
        int points = 0;
        
        for(Card aCard: this.hand.getListOfCards()) {
            points += aCard.getCardPoints();
        }
        
        return points;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (playerId != null ? playerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Player)) {
            return false;
        }
        Player other = (Player) object;
        if ((this.playerId == null && other.playerId != null) || (this.playerId != null && !this.playerId.equals(other.playerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Player[ id=" + playerId + " ]";
    }
    
}

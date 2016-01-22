/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Sryn
 */
@Entity
public class SubGame implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subGameId;

    @ManyToOne
    private Game game;
    
    @OneToMany
    private List<Player> subGamePlayers;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date subGameTimeStamp;
    
    @OneToOne
    private CardList drawPile;

    @OneToOne
    private CardList discardPile;
    
    @OneToOne
    private Player currentPlayer;

    private List<Boolean> directionList; // true = clockwise/ascending-index, false = anti-clockwise/descending-index
    
    private String subGameStatus; // waitingToStart, started, suspended, finished

    public Long getSubGameId() {
        return subGameId;
    }

    public void setSubGameId(Long subGameId) {
        this.subGameId = subGameId;
    }
    
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Player> getSubGamePlayers() {
        return subGamePlayers;
    }

    public void setSubGamePlayers(List<Player> subGamePlayers) {
        this.subGamePlayers = subGamePlayers;
    }

    public Date getSubGameTimeStamp() {
        return subGameTimeStamp;
    }
    
    public void setGameTimeStamp() {
        Calendar newCalendar = null;
        this.subGameTimeStamp = newCalendar.getTime();
    }
    
    public void setSubGameTimeStamp(Date subGameTimeStamp) {
        this.subGameTimeStamp = subGameTimeStamp;
    }
    
    public CardList getDrawPile() {
        return drawPile;
    }
    
    public void setDrawPile(CardList drawPile) {
        this.drawPile = drawPile;
        this.drawPile.setListType("drawPile");
    }
    
    public CardList getDiscardPile() {
        return discardPile;
    }
    
    public void setDiscardPile(CardList discardPile) {
        this.discardPile = discardPile;
        this.discardPile.setListType("discardPile");
    }

    public String getSubGameStatus() {
        return subGameStatus;
    }

    public void setSubGameStatus(String subGameStatus) {
        this.subGameStatus = subGameStatus;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Boolean> getDirectionList() {
        return directionList;
    }

    public void setDirectionList(List<Boolean> directionList) {
        this.directionList = directionList;
    }
    public void setupSubGame() {
        this.subGameStatus = "preparing";
        // automatically do setup process once a player has
        //  either clicked 'Start Game' 
        //  or previous subGame has 'finished'
        // get gamePlayers list from Game and populate subGamePlayers list
        // get shuffled drawPile
        // Note: there is no 'dealer' player
        // automatically determine 1st player by
        //  if 1st subgame, then draw one card each from drawPile, 
        //   player with highest number card becomes 1st player
        //   draws will result in the draw players drawing again
        //   (Action/Symbol cards ignored) 
        //  if not 1st subgame, previous subGame winner becomes 1st player
        // In subGamePlayers list, move all players to the left (earlier/lower)
        //  of 1st player to the end of the list
        // return all cards to drawPile and shuffle again
        // shuffle out 7 cards to each player one at a time in a clockwise
        //  (increasing index) fashion starting from the 1st player
        // draw a card from drawPile to discardPile
        //  if drawn card is wild or wild4, add back to drawPile, shuffle drawPile & draw again
        //  if drawn card is a number card, 1st player becomes next player
        //  if drawn card is an action/symbol card, act upon that card towards
        //   the 1st player to determine the next player
        // wait for next player to 'add' valid card from his/her hand to discardPile
        // set subGameStatus = "started"
    }
    
    public void startSubGame() {
        this.subGameStatus = "started";
        // set TimeStamp
    }
    
    public void suspendSubGame() {
        this.subGameStatus = "suspended";
        // when all players have left subgame before subGame winner is determined
    }
    
    public void finishSubGame() {
        this.subGameStatus = "finished";
        // once a player finishes all his cards
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subGameId != null ? subGameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubGame)) {
            return false;
        }
        SubGame other = (SubGame) object;
        if ((this.subGameId == null && other.subGameId != null) || (this.subGameId != null && !this.subGameId.equals(other.subGameId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Game[ id=" + subGameId + " ]";
    }

}

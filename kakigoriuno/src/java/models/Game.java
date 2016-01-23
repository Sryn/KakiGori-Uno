/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import static utilities.Utilities.*;

/**
 *
 * @author Sryn
 */
@Entity
//@Named="game"
//@ApplicationScoped
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gameId;
    
    private String gameName;

//    @OneToMany
//    private List<Player> gamePlayers0;
    
    private List<User> gamePlayers;
    
    private String gameStyle; // lowestPoints, firstTo500

    @OneToMany(mappedBy = "game")
    private List<SubGame> subGameList;

    private String gameStatus; // listed, waitingToStart, started, suspended, finished

    public Game() {
        super();
        this.setGameId(getRandomLong());
        String gameInstanceName = this.toString();
        this.gameName = gameInstanceName.substring(12);
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
    
//    public List<Player> getGamePlayers0() {
//        return gamePlayers0;
//    }
//
//    public void setGamePlayers0(List<Player> gamePlayers0) {
//        this.gamePlayers0 = gamePlayers0;
//    }
    
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    
    public List<User> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<User> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
    
    public String getGameStyle() {
        return gameStyle;
    }

    public void setGameStyle(String gameStyle) {
        this.gameStyle = gameStyle;
    }

    public List<SubGame> getSubGameList() {
        return subGameList;
    }

    public void setSubGameList(List<SubGame> subGameList) {
        this.subGameList = subGameList;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
    
    public void listGame() {
        this.gameStatus = "listed";
    }

    public boolean isListed() {
        return (this.gameStatus.equals("listed"));
    }
    
    public void setupGame() {
        this.gameStatus = "waitingToStart";
        // automatically do setup process once a player goes into an empty 'table'
        // add starting player to gamePlayers list
        // get starting player to select gameStyle
        // add joining player(s) to gamePlayers list
        // wait for starting player to start 1st subgame
        // then gameStatus = "started"
    }
    
    public boolean isWaiting() {
        return (this.gameStatus.equals("waitingToStart"));
    }
    
    public void startGame() {
        this.gameStatus = "started";
        // when 1st subGame has been started
    }
    
    public boolean isStarted() {
        return (this.gameStatus.equals("started"));
    }
    
    public void suspendGame() {
        this.gameStatus = "suspended";
        // when a subgame is suspended
    }
    
    public boolean isSuspended() {
        return (this.gameStatus.equals("suspended"));
    }
    
    public void finishGame() {
        this.gameStatus = "finished";
        // once a player has won according to gameStyle
    }
    
    public boolean isFinished() {
        return (this.gameStatus.equals("finished"));
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gameId != null ? gameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Game)) {
            return false;
        }
        Game other = (Game) object;
        if ((this.gameId == null && other.gameId != null) || (this.gameId != null && !this.gameId.equals(other.gameId))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "models.Game[ id=" + gameId + " ]";
//    }

}

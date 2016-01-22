/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Sryn
 */
//@Entity
@Named("gamesmap")
@ApplicationScoped
public class GamesMap implements Serializable {

//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    private Map<Long, Game> gamesMap = new HashMap<>();
//    private HashMap<Long, Game> gamesHashMap;

    @PostConstruct
    private void init() {
        System.out.println(">>> in post construct");
//        this.gamesMap = new HashMap<>();
//        this.gamesMap.
    }

    @PreDestroy
    private void cleanup() {
        System.out.println(">>> in pre destroy");
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
    public Map<Long, Game> getGamesMap() {
        return gamesMap;
    }

    public void setGamesMap(Map<Long, Game> gamesMap) {
        this.gamesMap = gamesMap;
    }
    
    public List<Game> getListOfGames() {
        List<Game> gamesList = new ArrayList();
        
        this.gamesMap.forEach((k,v) -> gamesList.add(v));
        
        return gamesList;
    }
    
    public Game getFirstGameOfType(String gameStatus) {
        List<Game> gamesList = getListOfGames();
        Game aGame = null;
        
        for(Game aGame2: gamesList) {
            if(aGame2.getGameStatus().matches(gameStatus)) {
                aGame = aGame2;
                break;
            }
        }
        
        return aGame;
    }

    public Long findListedGame() {
        // somehow, using this is dangerous

        Game aGame;
        Long gameId = null;
        Boolean listedGameFound = false;

        Iterator it = this.gamesMap.entrySet().iterator();

        while ((it.hasNext()) && (listedGameFound == false)) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(">>> findListedGame(): " + pair.getKey() + " = " + pair.getValue());
            aGame = (Game) pair.getValue();

            if (aGame.getGameStatus().matches("listed")) {
                listedGameFound = true;
                gameId = (Long) pair.getKey();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        System.out.println(">>> findListedGame() Long=" + gameId);
        return gameId;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof GamesMap)) {
//            return false;
//        }
//        GamesMap other = (GamesMap) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//    @Override
//    public String toString() {
//        return "models.GamesMap[ id=" + id + " ]";
//    }
    public Iterator entrySetIterator() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Iterator it = null;
        try {
            it = this.gamesMap.entrySet().iterator();
            System.out.println(">>> entrySetIterator() it=" + it.toString());
        } catch (NullPointerException ex) {
            System.out.println(">>> entrySetIterator() NullPointerException " + ex.toString());
        }
        return it;
    }

    public void put(Long gameId, Game newGame) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        try {
            this.gamesMap.put(gameId, newGame);
        } catch (NullPointerException ex) {
            System.out.println(">>> put NullPointerException " + ex.toString());
        }
        System.out.println(">>> gamesMap.size()=" + this.gamesMap.size());
    }

    public Game get(Long gameId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return (this.gamesMap.get(gameId));
    }

    public boolean isEmpty() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return (this.gamesMap.isEmpty());
    }

    public int size() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return (this.gamesMap.size());
    }

    public boolean containsKey(Long gameId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return (this.gamesMap.containsKey(gameId));
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import models.Card.Colour;
import models.CardList.CardListType;
import static utilities.Utilities.*;

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

//    @OneToOne
//    private Player nextPlayer;
    public enum Direction {
        CLOCKWISE,
        ANTICLOCKWISE
    }

    private List<Direction> directionList; // clockwise=ascending-index, anti-clockwise=descending-index

    private List<Colour> colourList;

    private int roundNo; // current game round number

    private SubGameStatus subGameStatus; // preparing, started, suspended, finished

    private Player subGameWinner;

    public enum SubGameStatus {
        PREPARED,
        STARTED,
        SUSPENDED,
        FINISHED
    }

    public SubGame() {
    }

    // constructor
    public SubGame(Game game, List<Player> subGamePlayers, int roundNo) {
        this.subGameId = getRandomLong();
        this.game = game;
        this.subGamePlayers = subGamePlayers;
        this.subGameTimeStamp = new java.util.Date();
        this.drawPile = new CardList(CardListType.DRAWPILE);
        this.discardPile = new CardList(CardListType.DISCARDPILE);
        this.directionList = new ArrayList();
        this.colourList = new ArrayList();
        this.roundNo = roundNo;
        this.setupSubGame();
    }

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
        this.drawPile.setListType(CardListType.DRAWPILE);
    }

    public CardList getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(CardList discardPile) {
        this.discardPile = discardPile;
        this.discardPile.setListType(CardListType.DISCARDPILE);
    }

    public int getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }

    public SubGameStatus getSubGameStatus() {
        return subGameStatus;
    }

    public void setSubGameStatus(SubGameStatus subGameStatus) {
        this.subGameStatus = subGameStatus;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

//    public Player getNextPlayer() {
//        return nextPlayer;
//    }
//
//    public void setNextPlayer(Player nextPlayer) {
//        this.nextPlayer = nextPlayer;
//    }
    public List<Direction> getDirectionList() {
        return directionList;
    }

    public void setDirectionList(List<Direction> directionList) {
        this.directionList = directionList;
    }

    public List<Colour> getColourList() {
        return colourList;
    }

    public void setColourList(List<Colour> colourList) {
        this.colourList = colourList;
    }

    public void setupSubGame() {
        this.subGameStatus = SubGameStatus.PREPARED;
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

    public boolean isPrepared() {
        return (this.subGameStatus.equals(SubGameStatus.PREPARED));
    }

    public void startSubGame() {
        this.subGameStatus = SubGameStatus.STARTED;
        // set TimeStamp
    }

    public boolean isStarted() {
        return (this.subGameStatus.equals(SubGameStatus.STARTED));
    }

    public void suspendSubGame() {
        this.subGameStatus = SubGameStatus.SUSPENDED;
        // when all players have left subgame before subGame winner is determined
    }

    public boolean isSuspended() {
        return (this.subGameStatus.equals(SubGameStatus.SUSPENDED));
    }

    public void finishSubGame() {
        this.subGameStatus = SubGameStatus.FINISHED;
        // once a player finishes all his cards
    }

    public boolean isFinished() {
        return (this.subGameStatus.equals(SubGameStatus.FINISHED));
    }

    public Player getSubGameWinner() {
        return subGameWinner;
    }

    public void setSubGameWinner(Player subGameWinner) {
        this.subGameWinner = subGameWinner;
    }

//    public void moveBackAllCardsToDrawPile(List<Player> firstRoundPlayers, SubGame currentRound) {
    public void moveBackAllCardsToDrawPile() {
        // move all cards from each player's hand back to the drawPile
//        List <Player> firstRoundPlayers = this.subGamePlayers;
//        SubGame currentRound = this;

        for (Player aPlayer : this.subGamePlayers) {
            CardList currentHand = aPlayer.getHand();
            while (!currentHand.getListOfCards().isEmpty()) {
                this.getDrawPile().addCard(currentHand.drawCard());
            }
        }
        this.getDrawPile().shuffleCards();
    }

    // not working. stays in a loop at the second round !!!
//    public void movePlayersBeforeFirstPlayerToEndOfList() {
//        int i = 0;
//        Player privCurrentPlayer;
//        List<Player> tempList = new ArrayList();
////        List<Player> tempOrig = new ArrayList(this.subGamePlayers);
//        List<Player> tempOrig = new ArrayList();
//
////        Collections.copy(tempOrig, this.subGamePlayers);
//        
//        for(Player aPlayer: this.subGamePlayers) {
//            tempOrig.add(aPlayer);
//        }
//
////        while(currentPlayer != this.getCurrentPlayer()) {
////            currentPlayer = this.subGamePlayers.remove(i);
////            this.subGamePlayers.add(currentPlayer);
////            i++;
////            currentPlayer = this.subGamePlayers.get(i);
////        }
//        // move players before currentPlayer to tempList
//        for (Player aPlayer : tempOrig) {
//            if (aPlayer != this.getCurrentPlayer()) {
////                currentPlayer = this.subGamePlayers.remove(i++);
//                currentPlayer = this.subGamePlayers.remove(0);
//                tempList.add(currentPlayer);
//            } else {
//                break;
//            }
//        }
//
//        // move players in tempList back to the end of currentGame SubPlayersList
//        for (Player aPlayer : tempList) {
//            this.subGamePlayers.add(aPlayer);
//        }

//        Boolean notDone = true;
//
//        if (!this.subGamePlayers.isEmpty()) {
//            if (this.subGamePlayers.get(0) != this.currentPlayer) {
////                while (this.subGamePlayers.get(0) != this.currentPlayer) {
//                while (notDone) {
//                    privCurrentPlayer = this.subGamePlayers.remove(0);
//                    if (privCurrentPlayer == this.currentPlayer) {
//                        notDone = false;
//                        this.subGamePlayers.add(0, privCurrentPlayer);
//                    } else {
//                        System.out.println(">> subGame movePlayersBeforeFirstPlayerToEndOfList currentPlayer = "
//                                + this.currentPlayer.getPlayer().getUsername()
//                                + " privCurrentPlayer = "
//                                + privCurrentPlayer.getPlayer().getUsername());
//                        this.subGamePlayers.add(privCurrentPlayer);
//                    }
//                }
//            }
//        }
//
//    }

    public String getPlayersListText() {
        String rtnString = "";
        for (Player aPlayer : this.getSubGamePlayers()) {
            rtnString = rtnString.concat("\t\t>> ");
            rtnString = rtnString.concat(aPlayer.getPlayer().getUsername());
            rtnString = rtnString.concat("\n");
        }
        return rtnString;
    }

    public void dealOutOneCardEachToEveryPlayer() {
        Card aCard;

        for (Player aPlayer : this.getSubGamePlayers()) {
            aCard = this.drawPile.drawCard();
            aPlayer.getHand().addCard(aCard);
        }

    }

    public void dealOutSevenCardsToEveryPlayer() {
        for (int i = 1; i <= 7; i++) {
            this.dealOutOneCardEachToEveryPlayer();
        }

    }

    public String listAllCardListSizes() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String rtnString = "";
        rtnString = rtnString.concat("\t\tDrawPile    = " + Integer.toString(this.drawPile.getListOfCards().size()) + "\n");
        rtnString = rtnString.concat("\t\tDiscardPile = " + Integer.toString(this.discardPile.getListOfCards().size()) + "\n");
        for (Player aPlayer : this.subGamePlayers) {
            rtnString = rtnString.concat(
                    "\t\t" + aPlayer.getPlayer().getUsername()
                    + " Hand = " + Integer.toString(aPlayer.getHand().getListOfCards().size())
                    + "\n");
        }
        return rtnString;
    }

    public Boolean drawOneCardFromDrawPileToDiscardPile() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Card aCard;

        if (this.drawPile.getListOfCards().isEmpty()) {
            // drawPile empty, so refill drawPile with the cards in discardPile except its top card
            if (refillDrawPile()) {
                return true;
            } else {
                // cannot deal out
                return false;
            }
        } else {
            aCard = this.drawPile.drawCard();
            this.discardPile.addCard(aCard);
            return true;
        }
    }

    public String showDrawAndDiscardPilesTopCards() {
        String rtnString = "";

        rtnString = rtnString.concat("\t\tDrawPile    top card = " + listCardListTopCard(this.getDrawPile()) + "\n");
        rtnString = rtnString.concat("\t\tDiscardPile top card = " + listCardListTopCard(this.getDiscardPile()) + "\n");

        return rtnString;
    }

    private String listCardListTopCard(CardList aList) {
        if (aList.getListOfCards().isEmpty()) {
            return "Empty List";
        } else {
            return (aList.getListOfCards().get(0).toString());
        }
    }

    public boolean refillDrawPile() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if ((this.discardPile.getListOfCards().isEmpty()) || (this.discardPile.getListOfCards().size() == 1)) {
            // error: discardPile empty or just one card there
            System.out.println("### Error: discardPile size=" + this.discardPile.getListOfCards().size());
            return false;
        } else {
            Card aCard;

            aCard = this.discardPile.drawCard(); // save the discardPile top card
            moveAllCards(this.discardPile, this.drawPile);
            this.discardPile.addCard(aCard); // put back the discardPile top card

            this.drawPile.shuffleCards();

            return true;
        }
    }

    public void moveAllCards(CardList fromList, CardList destList) {

        Card aCard;

        while (!(fromList.getListOfCards().isEmpty())) {
            aCard = fromList.drawCard();
            destList.addCard(aCard);
        }
    }

    public void givePlayer2Cards(Player currentPlayer) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.givePlayerXCards(currentPlayer, 2);
    }

    public void givePlayer4Cards(Player currentPlayer) {
        this.givePlayerXCards(currentPlayer, 4);
    }

    private void givePlayerXCards(Player currentPlayer, int noOfCardsToGive) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Card aCard;

        for (int i = 0; i < noOfCardsToGive; i++) {
            aCard = this.getTopCardFromDrawPile();
            currentPlayer.getHand().addCard(aCard);
        }
    }

    private Card getTopCardFromDrawPile() {
        return this.drawPile.drawCard();
    }

    public Player getLastPlayer() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (this.subGamePlayers.isEmpty()) {
            return null;
        } else {
            return (this.subGamePlayers.get(this.subGamePlayers.size() - 1));
        }
    }

    public Player getNextPlayer(Player currentPlayer) {

        Player privNextPlayer = null;
        int dirListSize = this.getDirectionList().size(), currentPlayerIndex, nextPlayerIndex;

        currentPlayerIndex = this.getSubGamePlayers().indexOf(currentPlayer);

        if (dirListSize == 0) {
            // error: DirectionList size shouldn't be 0
            System.out.println("### Error: directionList size = " + dirListSize);
        } else if (currentPlayerIndex == -1) {
            // error: currentPlayer not found in list
            System.out.println("### Error: Not found in current SubGamePlayersList = " + currentPlayer.toString());
        } else {
            if (getLastDirection().equals(Direction.CLOCKWISE)) {
                // if true, means clockwise or increasing index

                nextPlayerIndex = currentPlayerIndex + 1;

                // nextPlayerIndex is more than the index of the last player
                if (nextPlayerIndex >= this.getSubGamePlayers().size()) {
                    // go back to front of list
                    nextPlayerIndex = nextPlayerIndex - this.getSubGamePlayers().size();
                }
            } else {
                // false, therefore anti-clockwise or decreasing index
                nextPlayerIndex = currentPlayerIndex - 1;

                // nextPlayerIndex is less than the index of the first player
                if (nextPlayerIndex < 0) {
                    // go back to back of list
                    nextPlayerIndex = this.getSubGamePlayers().size() + nextPlayerIndex;
                }
            }

            privNextPlayer = this.subGamePlayers.get(nextPlayerIndex);
        }

        return privNextPlayer;
    }

    public Direction getLastDirection() {
        if (this.directionList.isEmpty()) {
            return null;
        } else {
            int dirListSize = this.directionList.size();

            return (this.directionList.get(dirListSize - 1));
        }
    }

    public Colour getLastColour() {
        if (this.colourList.isEmpty()) {
            return null;
        } else {
            int colListSize = this.colourList.size();

            return (this.colourList.get(colListSize - 1));
        }
    }

    public Player getAfterSkipPlayer(Player currentPlayer) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Player afterSkipPlayer;

        afterSkipPlayer = getPlayerAfterNextPlayerIsSkipped(currentPlayer);

        return afterSkipPlayer;
    }

    public Player getAfterDraw2Player(Player currentPlayer) {
        Player afterDraw2Player;

        afterDraw2Player = getPlayerAfterNextPlayerIsSkipped(currentPlayer);

        return afterDraw2Player;
    }

    private Player getPlayerAfterNextPlayerIsSkipped(Player currentPlayer) {
        Player afterSkippedPlayer, privNextPlayer;

        privNextPlayer = getNextPlayer(currentPlayer);

        afterSkippedPlayer = getNextPlayer(privNextPlayer);

        return afterSkippedPlayer;
    }

    public Player getPlayerFromUserObject(User aUser) {
        for (Player aPlayer : this.subGamePlayers) {
            if (aPlayer.getPlayer() == aUser) {
                return aPlayer;
            }
        }

        return null;
    }

    public Player processCardPlayedAndGetNextPlayer(Card chosenCard, String colourChoice) {
        Player nextPlayer = null, normalNextPlayer, privCurrentPlayer;
        Direction currentDirection;
        Colour chosenColour, chosenCardColour;

        currentDirection = this.getLastDirection();

        privCurrentPlayer = this.currentPlayer;
        normalNextPlayer = this.getNextPlayer(privCurrentPlayer);

        chosenCardColour = chosenCard.getCardColour();

        switch (colourChoice) {
            case "red":
                chosenColour = Colour.RED;
                break;
            case "yellow":
                chosenColour = Colour.YELLOW;
                break;
            case "green":
                chosenColour = Colour.GREEN;
                break;
            case "blue":
                chosenColour = Colour.BLUE;
                break;
            default:
                chosenColour = null;
                break;
        }

        switch (chosenCard.getCardAction()) {
            case NUMBER:
                // normal operation
                this.directionList.add(currentDirection);
                this.colourList.add(chosenCardColour);
                nextPlayer = normalNextPlayer;
                break;
            case SKIP:
                // skip the next player
                this.directionList.add(currentDirection);
                this.colourList.add(chosenCardColour);
                nextPlayer = this.getAfterSkipPlayer(privCurrentPlayer);
                break;
            case REVERSE:
                // reverse direction
                if (currentDirection.equals(Direction.CLOCKWISE)) {
                    this.directionList.add(Direction.ANTICLOCKWISE);
                } else {
                    this.directionList.add(Direction.CLOCKWISE);
                }
                this.colourList.add(chosenCardColour);
                // there's a special case in a two-players subGame/round where a reverse is like a skip
                if (this.getSubGamePlayers().size() == 2) // play returns to the currentPlayer
                {
                    nextPlayer = this.getAfterSkipPlayer(privCurrentPlayer);
                } else // not a two-players game
                // get new next player after direction change
                {
                    nextPlayer = this.getNextPlayer(this.currentPlayer);
                }
                break;
            case DRAW2:
                this.directionList.add(currentDirection);
                this.colourList.add(chosenCardColour);
                // give next player 2 cards from drawPile
                this.givePlayer2Cards(normalNextPlayer);
                // skip next player
                nextPlayer = this.getNextPlayer(normalNextPlayer);
                break;
            case WILD: // unfinished
                this.directionList.add(currentDirection);
                // currentPlayer have to choose amongst the 4 colours
                this.colourList.add(chosenColour);
                // next player has to play a card from that colour or Wild or WildDraw4
                nextPlayer = normalNextPlayer;
                break;
            case WILD_DRAW4: // unfinished
                this.directionList.add(currentDirection);
                // currentPlayer have to choose amongst the 4 colours
                this.colourList.add(chosenColour);
                // give next player 4 cards from drawPile
                this.givePlayer4Cards(normalNextPlayer);
                // skip next player
                nextPlayer = this.getNextPlayer(normalNextPlayer);
                // next player has to play a card from that colour or Wild or WildDraw4
                break;
            default:
                System.out.println("### Error: Unknown switchAction");
                break;
        }

        return nextPlayer;
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

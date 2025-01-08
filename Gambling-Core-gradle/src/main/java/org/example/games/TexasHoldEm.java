package org.example.games;

import org.example.Card;

import javax.swing.*;
import java.util.Scanner;

public class TexasHoldEm extends Game{

    enum playerAbilities{CALL, RAISE, FOLD}
    enum players{PLAYER, BOTONE, BOTTWO, BOTTHREE}

    public static double playerMoney = 2500.00;
    public static double botBetCap = 2500.00;


    public static double bettingPower = 2;
    public static double bet = 0;
    public static double betIncrease = 0;
    public static boolean newRoundOfBetting = false;

    static Scanner console = new Scanner(System.in);

    public static Hand playerHand = new Hand(false, "Player Hand");
    public static Hand river = new Hand(false, "The River");
    public static Hand botHand1 = new Hand(false, "Bot Hand 1");
    public static Hand botHand2 = new Hand(false, "Bot Hand 2");
    public static Hand botHand3 = new Hand(false, "Bot Hand 3");

    JPanel tempPanel = new JPanel();

    public TexasHoldEm(){
        this.add(tempPanel);
        tempPanel.add(new JLabel("Texas Hold Em, Currently Text only"));

        createNewDeck(1);
        playTextPoker();
    }

    private void playTextPoker() {
        dealCards();

        while (true) {
            for (int i = 0; i < 4; i++) {
                switch (i){
                    case 0 -> {
                        //pre Flop
                        makeBet(true,false, players.PLAYER, false, false);
                        makeBet(false,true, players.BOTONE, false, false);
                        makeBet(false,false, players.BOTTWO, false, false);
                        makeBet(false,false, players.BOTTHREE, false, false);
                        betIncrease = 0;
                        postBlinds(players.PLAYER);
                    }
                }
            }
        }
    }

    private void postBlinds(players playerID) {
        switch (playerID) {
            case PLAYER -> {
                makeBet(false, false, players.PLAYER, false, true);
                if (newRoundOfBetting){
                    repeatRound(playerID);
                }
                flop(3);
                //initial flop
                makeBet(false, false, players.BOTONE, true, false);
                makeBet(false, false, players.BOTTWO, true, false);
                makeBet(false, false, players.BOTTHREE, true, false);
                makeBet(false, false, players.PLAYER, true, false);
                flop(1);
                makeBet(false, false, players.BOTONE, true, false);
                makeBet(false, false, players.BOTTWO, true, false);
                makeBet(false, false, players.BOTTHREE, true, false);
                makeBet(false, false, players.PLAYER, true, false);
                flop(1);
                makeBet(false, false, players.BOTONE, true, false);
                makeBet(false, false, players.BOTTWO, true, false);
                makeBet(false, false, players.BOTTHREE, true, false);
                makeBet(false, false, players.PLAYER, true, false);
            }
        }

    }

    private void repeatRound(players playerID) {
        switch (playerID){
            case PLAYER -> {
                makeBet(false, false, players.BOTONE, true, false);
                makeBet(false, false, players.BOTTWO, true, false);
                makeBet(false, false, players.BOTTHREE, true, false);
                makeBet(false, false, players.PLAYER, true, true);
            }
        }
    }

    private void flop(int cardsFliped) {

        for (int i = 0; i < cardsFliped; i++) {
            Card newCard = drawCards();
            river.add(newCard);
        }

        loopThroughOpeningHand(playerHand);
        loopThroughRiverAndHand(playerHand);
        loopThroughOpeningHand(botHand1);
        loopThroughRiverAndHand(botHand1);
        loopThroughOpeningHand(botHand2);
        loopThroughRiverAndHand(botHand2);
        loopThroughOpeningHand(botHand3);
        loopThroughRiverAndHand(botHand3);

    }

    private void makeBet(boolean isBigBlind, boolean isSmallBlind, players playerID, boolean afterFlop, boolean fullCircle) {

        if (!afterFlop) {
            if (isBigBlind && !fullCircle) {
                bet = 50;
                betIncrease = 50;
                if (playerID == players.PLAYER){
                    playerMoney -= 50;
                }
            } else if (isSmallBlind) {
                bet += betIncrease/2;

                switch (playerID) {
                    case players.PLAYER -> {
                        playerMoney -= 25;
                        while (true) {
                            System.out.println("Call, or Fold");
                            String userInput = console.nextLine();
                            if (userInput.equalsIgnoreCase("call")) {
                                bet += betIncrease/2;
                                playerMoney -= 25;
                                printMove(playerAbilities.CALL, playerID);
                                break;
                            } else if (userInput.equalsIgnoreCase("fold")) {
                                quickEndGame();
                                break;
                            } else {
                                System.out.println("ERROR INCORRECT INPUT, TRY AGAIN");
                            }
                        }
                    }
                    case players.BOTONE, players.BOTTWO -> {
                        bet += betIncrease;
                        printMove(playerAbilities.CALL, playerID);
                    }
                    case BOTTHREE -> { // THE CHEATING BOT
                        bet += betIncrease;
                        printMove(playerAbilities.CALL, playerID);
                    }
                }
            } else if (fullCircle){
                switch (playerID) {
                    case players.PLAYER -> {
                        while (true) {
                            System.out.println("Call, Raise or Fold");
                            String userInput = console.nextLine();
                            if (userInput.equalsIgnoreCase("call")) {
                                printMove(playerAbilities.CALL, playerID);
                                break;
                            } else if (userInput.equalsIgnoreCase("fold")) {
                                quickEndGame();
                                break;
                            } else if (userInput.equalsIgnoreCase("raise")) {
                                if (playerMoney > 0) {
                                    while (true) {
                                        System.out.println("How Much would you like to raise?");
                                        userInput = console.nextLine();
                                        try {
                                            if (Integer.parseInt(userInput) > 0 && Integer.parseInt(userInput) < playerMoney) {
                                                newRoundOfBetting = true;
                                                bet += Integer.parseInt(userInput);
                                                betIncrease = Integer.parseInt(userInput);
                                                playerMoney -= betIncrease;
                                                return;
                                            } else {
                                                System.out.println("Invalid Input try again CASE: MONEY");
                                            }
                                        } catch (NumberFormatException Ex) {
                                            System.out.println("Invalid Input try again CASE: NUMPARSEFAIL");
                                        }

                                    }
                                } else {
                                    break;
                                }
                            } else {
                                System.out.println("Invalid Input try again CASE: NO CRF"); //Call Raise Fold
                            }
                        }
                    }
                    case players.BOTONE, players.BOTTWO -> {
                        printMove(playerAbilities.CALL, playerID);
                        System.out.println("BET NOT CHANGED ERROR 1");
                    }
                    case BOTTHREE -> { // THE CHEATING BOT
                        printMove(playerAbilities.CALL, playerID);
                        System.out.println("BET NOT CHANGED ERROR 2");
                    }
                }
            }
        } else {
            switch (playerID) {
                case players.PLAYER -> {
                    while (true) {
                        System.out.println("Call, Raise or Fold");
                        String userInput = console.nextLine();
                        if (userInput.equalsIgnoreCase("call")) {
                            printMove(playerAbilities.CALL, playerID);
                            break;
                        } else if (userInput.equalsIgnoreCase("fold")) {
                            quickEndGame();
                            break;
                        } else if (userInput.equalsIgnoreCase("raise")) {
                            if (playerMoney > 0) {
                                while (true) {
                                    System.out.println("How Much would you like to raise?");
                                    userInput = console.nextLine();
                                    try {
                                        if (Integer.parseInt(userInput) > 0 && Integer.parseInt(userInput) < playerMoney) {
                                            newRoundOfBetting = true;
                                            bet += Integer.parseInt(userInput);
                                            betIncrease = Integer.parseInt(userInput);
                                            playerMoney -= betIncrease;
                                            return;
                                        } else {
                                            System.out.println("Invalid Input try again CASE: MONEY");
                                        }
                                    } catch (NumberFormatException Ex) {
                                        System.out.println("Invalid Input try again CASE: NUMPARSEFAIL");
                                    }

                                }
                            } else {
                                break;
                            }
                        } else {
                            System.out.println("Invalid Input try again CASE: NO CRF"); //Call Raise Fold
                        }
                    }
                }
                case players.BOTONE, players.BOTTWO -> {
                    printMove(playerAbilities.CALL, playerID);
                    System.out.println("BET NOT CHANGED ERROR 1");
                }
                case BOTTHREE -> { // THE CHEATING BOT
                    printMove(playerAbilities.CALL, playerID);
                    System.out.println("BET NOT CHANGED ERROR 2");
                }
            }
        }
    }

    private void quickEndGame() {
        System.out.println("ERROR NOT COMPLETE");
    }

    private void printMove(playerAbilities playerAbilities, players playerID) {
        switch (playerAbilities){
            case TexasHoldEm.playerAbilities.CALL -> {
                String player = getPlayerName(playerID);
                if (playerID != players.PLAYER) {
                    System.out.println(player + "Calls." + '\n' + "Current betting pool is " + bet);
                } else {
                    System.out.println("You Call." + '\n' + "Current betting pool is " + bet);
                }
            }
        }
    }

    private String getPlayerName(players playerID) {
        switch (playerID){
            case players.PLAYER -> {
                return "You";
            }
            case BOTONE -> {
                return "Bot One";
            }
            case BOTTWO -> {
                return "Bot Two";
            }
            case BOTTHREE -> {
                return "Bot Three";
            }
            default -> {
                return "ERROR PLAYERID";
            }
        }
    }

    /**
     * Draws 2 cards and gives them to either the player or the dealer
     * <p> Calls: {@link Game#drawCards()} {@link TexasHoldEm#loopThroughOpeningHand(Hand)}
     */
    private static void dealCards(){

        for (int i = 0; i < 4; i++){
            Card drawnCard = drawCards();
            Card drawnCard2 = drawCards();

            switch (i){
                case 0 -> {
                    playerHand.add(drawnCard);
                    playerHand.add(drawnCard2);
                    loopThroughOpeningHand(playerHand);
                }
                case 1 -> {
                    botHand1.add(drawnCard);
                    botHand1.add(drawnCard2);
                    loopThroughOpeningHand(botHand1);
                }
                case 2 -> {
                    botHand2.add(drawnCard);
                    botHand2.add(drawnCard2);
                    loopThroughOpeningHand(botHand2);
                }
                case 3 -> {
                    botHand3.add(drawnCard);
                    botHand3.add(drawnCard2);
                    loopThroughOpeningHand(botHand3);
                }
            }
        }
    }

    /**
     * Loops through a list of cards, totals the value of those card
     * <p> Calls: {@link TexasHoldEm#parseCard(Card)}
     * @param hand That gets looped through
     */
    private static void loopThroughOpeningHand(Hand hand) {
        for (Card card : hand) {
            int tempValue = parseCard(card);
            if (tempValue > hand.highCard){
                hand.highCard = tempValue;
            } else if (tempValue == hand.highCard) {
                hand.hasOnePair = true;
            } else if (tempValue < 0) {
                System.out.println("ERROR CATCH 1");
            }

            if (hand == playerHand){
                printPlayerCards(playerHand);
            }
        }
    }

    /**
     * Checks the param hand for all possible winning hands
     *
     * NEEDS TESTING SOME MISSING CASES
     *
     * @param hand to check
     */
    private static void loopThroughRiverAndHand(Hand hand){
        for (Card cardInRiver : river) {
            int riverCardValue = parseCard(cardInRiver);
            for (Card cardInHand : hand){
                int handCardValue = parseCard(cardInHand);
                if ( riverCardValue == handCardValue){
                    if (hand.hasOnePair && riverCardValue == hand.onePairValue && !hand.hasThreeOfAKind){
                        hand.hasThreeOfAKind = true;
                        hand.threeOfAKindValue = hand.onePairValue;
                    } else if (hand.hasThreeOfAKind && riverCardValue == hand.threeOfAKindValue) {
                        hand.hasFourOfAKind = true;
                        hand.fourOfAKindValue = hand.threeOfAKindValue;
                    } else if (hand.hasThreeOfAKind) {
                        hand.hasFullHouse = true;
                        hand.fullHouseTOKValue = hand.threeOfAKindValue;
                        hand.fullHousePairValue = riverCardValue;
                    } else if (hand.hasTwoPair) {
                        hand.hasFullHouse = true;
                        hand.fullHouseTOKValue = riverCardValue;
                        if (riverCardValue == hand.twoPairValueOne) {
                            hand.fullHousePairValue = hand.twoPairValueOne;
                        } else {
                            hand.fullHousePairValue = hand.twoPairValueTwo;
                        }
                    } else if (hand.hasOnePair) {
                        hand.hasTwoPair = true;
                        hand.twoPairValueOne = riverCardValue;
                        hand.twoPairValueTwo = hand.onePairValue;
                        hand.hasOnePair = false;
                    }else {
                        hand.hasOnePair = true;
                    }
                }
            }
        }
    }

    /**
     * Prints the cards for a certain hand
     * @param hand the hand to reveal
     */
    private static void printPlayerCards(Hand hand) {
        for (Card card : hand) {
            System.out.print(card.value + " of " + card.suit + "\n");
        }
    }

    /**
     * Checks a card's value then returns it numerically
     * @param card The card that gets its value checked
     * @return The int value of a card
     */
    private static int parseCard(Card card) {
        try {
            return Integer.parseInt(card.value);
        }catch (NumberFormatException ex){
            switch (card.value) {
                case "JACK" -> {
                    return  11;
                }
                case "QUEEN" -> {
                    return 12;
                }
                case "KING" -> {
                    return 13;
                }
                case "ACE" -> {
                    return  14;
                }
                default -> {
                    System.out.println("ERROR DATA FAILED 1");
                    return -99999;
                }
            }
        }
    }
}

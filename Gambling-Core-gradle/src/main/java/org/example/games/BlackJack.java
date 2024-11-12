package org.example.games;

import org.example.Card;

import java.util.ArrayList;
import java.util.Scanner;

public class BlackJack extends Game{

    enum MatchEnd {IGNORE, BUST, BLACKJACK}

    public static double money = 2500.00;
    public static double bettingPower = 2;
    public static double bet = 0;

    static Scanner console = new Scanner(System.in);

    public static ArrayList<Card> userHand = new ArrayList<>();
    static int userHandTotal = 0;
    static boolean userHasAce = false;

    public static ArrayList <Card> dealerHand = new ArrayList<>();
    static int dealerHandTotal = 0;
    static boolean dealerHasAce = false;

    public BlackJack(){
        dataWriter.setPrettyPrinting();
        createNewDeck();
    }

    /**
     * draws 2 cards and gives them to either the player or the dealer
     * @param toPlayer weather or not it goes to the player or dealer
     */
    public static void startPlaying(boolean toPlayer){

        Card drawnCard = drawCards();
        Card drawnCard2 = drawCards();

        if (toPlayer){
            userHand.add(drawnCard);
            userHand.add(drawnCard2);

            for (Card card : userHand) {
                try {
                    int cardValue = Integer.parseInt(card.value);
                    if (cardValue == 1) {
                        cardValue += 10;
                        userHasAce = true;
                    }
                    userHandTotal += cardValue;
                    if (userHasAce && userHandTotal > 21) {
                        userHandTotal -= 10;
                    }
                } catch (NumberFormatException ex) {
                    switch (card.value) {
                        case "ACE" -> {
                            userHandTotal += 1;
                            userHasAce = true;
                        }
                        case "JACK", "QUEEN", "KING" -> userHandTotal += 10;
                        default -> System.out.println("ERROR DATA FAILED 1");
                    }
                }
            }

        }else {
            dealerHand.add(drawnCard);
            dealerHand.add(drawnCard2);

            for (Card card : dealerHand) {
                try {
                    int cardValue = Integer.parseInt(card.value);
                    if (cardValue == 1) {
                        cardValue += 10;
                        dealerHasAce = true;
                    }
                    dealerHandTotal += cardValue;

                } catch (NumberFormatException ex) {
                    switch (card.value) {
                        case "ACE" -> {
                            dealerHandTotal += 1;
                            dealerHasAce = true;
                        }
                        case "JACK", "QUEEN", "KING" -> userHandTotal += 10;
                        default -> System.out.println("ERROR DATA FAILED 1");
                    }
                }
            }
        }
    }

    /**
     * gets user input on decisions of what to do, double, hit, stand, split
     *
     * Calls: printKnownCards() startBetting() bustOrBJ() userDouble() userHit()
     * checkForWin() cleanHands() checkForDuplicateCards()
     */
    public static void getUserInput(){

        boolean isFirstAsk = true;
        boolean duplicateCards = checkForDuplicateCards();

        printKnownCards();
        startBetting();

        switch (bustOrBJ(userHandTotal, userHasAce, true)){
            case MatchEnd.BUST, MatchEnd.BLACKJACK -> {return;}
        }

        switch (bustOrBJ(dealerHandTotal, dealerHasAce, false)){
            case MatchEnd.BUST, MatchEnd.BLACKJACK -> {return;}
        }

        while(true) {
            printKnownCards();

            if (isFirstAsk && duplicateCards) {
                System.out.println("Would you like to hit, stand, split, or double?");
            } else if (isFirstAsk) {
                System.out.println("Would you like to hit, stand, or double?");
            } else {
                System.out.println("Would you like to hit or stand?");
            }

            String userInput = console.nextLine();

            if (userInput.equals("double") || userInput.equals("hit") || userInput.equals("stand") ||
                            userInput.equals("split")){
                boolean verify = true;
            }else {
                System.out.println("Invalid input");
                continue;
            }

            if (isFirstAsk && duplicateCards && userInput.equals("split")){
                userSplitHand();
                continue;
            }else if (isFirstAsk && userInput.equals("double")){
                userDouble();
                return;
            }else if (userInput.equals("double")) {
                System.out.println("Invalid input");
                continue;
            }else if (userInput.equals("hit")){
                userHit();
                switch (bustOrBJ(userHandTotal, userHasAce, true)){
                    case MatchEnd.BUST, MatchEnd.BLACKJACK -> {return;}
                }
            } else {
                checkForWin();
                cleanHands();
                return;
            }

            if (isFirstAsk){
                isFirstAsk = false;
            }
        }
    }

    private static void userSplitHand() {
        
    }

    private static boolean checkForDuplicateCards() {
        int card1 = 0;
        int card2 = 0;

        try {
            card1 = Integer.parseInt(userHand.get(0).value);
        }catch (NumberFormatException ex){
            switch (userHand.getFirst().value) {
                case "ACE" -> card1 = 1;
                case "JACK", "QUEEN", "KING" -> card1 = 10;
                default -> System.out.println("ERROR DATA FAILED 1");
            }
        }

        try {
            card2 = Integer.parseInt(userHand.get(1).value);
        }catch (NumberFormatException ex){
            switch (userHand.get(1).value) {
                case "ACE" -> card2 = 1;
                case "JACK", "QUEEN", "KING" -> card2 = 10;
                default -> System.out.println("ERROR DATA FAILED 1");
            }
        }
        return card1 == card2;
    }

    private static MatchEnd bustOrBJ(int handTotal, boolean hasAce, boolean isPlayer) {

        if (handTotal > 21){
            if (hasAce){
                if (isPlayer) {
                    userHandTotal -= 10;
                    handTotal -= 10;
                }else {
                    dealerHandTotal -= 10;
                    handTotal -= 10;
                }
            }else {
                if (isPlayer) {
                    System.out.println("Busted, You Lose");
                } else {
                    System.out.println("Dealer Busted, You Win");
                    money += bet * bettingPower;
                }
                cleanHands();
                return MatchEnd.BUST;
            }
        }
        if (handTotal == 21) {
            if (isPlayer) {
                System.out.println("Black Jack, You Win");
                money += bet * bettingPower;
            }else {
                System.out.println("Dealer Black Jack, Dealer Win");
            }
            cleanHands();
            return MatchEnd.BLACKJACK;
        }

        return MatchEnd.IGNORE;
    }

    private static void userHit() {

        Card drawnCard = drawCards();

        userHand.add(drawnCard);
        try {
            userHandTotal += Integer.parseInt(drawnCard.value);
        } catch (NumberFormatException ex){
            switch (drawnCard.value){
                case "ACE" -> userHandTotal += 1;
                case "JACK", "QUEEN", "KING" -> userHandTotal += 10;
                default -> System.out.println("ERROR DATA FAILED 4");
            }
        }

        System.out.println("You drew a " + drawnCard.value + " of " + drawnCard.suit);

    }

    private static void userDouble() {
        Card drawnCard = drawCards();

        userHand.add(drawnCard);

        try {
            userHandTotal += Integer.parseInt(drawnCard.value);
        } catch (NumberFormatException ex){
            switch (drawnCard.value){
                case "ACE" -> userHandTotal += 1;
                case "JACK", "QUEEN", "KING" -> userHandTotal += 10;
                default -> System.out.println("ERROR DATA FAILED 3");
            }
        }

        System.out.println("You drew a " + drawnCard.value + " of " + drawnCard.suit);

        switch (bustOrBJ(userHandTotal, userHasAce, true)){
            case MatchEnd.BUST -> {return;}
            case MatchEnd.BLACKJACK -> {
                money += bet * bettingPower;
                return;
            }
        }

        checkForWin();
        cleanHands();
    }

    private static void startBetting() {
        while(true) {
            System.out.println("How much do you want to bet?" + "\n");
            System.out.println("\033[3mYou currently have $\033[0m" + money);

            String betAmount = console.nextLine();
            try {
                bet = Double.parseDouble(betAmount);
                if (bet > money){
                    System.out.println("You don't have enough money");
                    continue;
                }
                if (bet > 5) {
                    money -= bet;
                    return;
                }else {
                    System.out.println("Bet at least $5");
                }
            } catch (NumberFormatException ex) {
                System.out.println("NOT A CASH VALUE TRY AGAIN");
            }
        }
    }

    private static void checkForWin() {

        while (dealerHandTotal < 17) {
            Card dealerCard = drawCards();
            try {
                dealerHandTotal += Integer.parseInt(dealerCard.value);
            } catch (NumberFormatException ex){
                switch (dealerCard.value){
                    case "ACE" -> dealerHandTotal += 1;
                    case "JACK", "QUEEN", "KING" -> userHandTotal += 10;
                    default -> System.out.println("ERROR DATA FAILED 5");
                }
            }

            dealerHand.add(dealerCard);
        }

        if (dealerHandTotal > 21){
            System.out.println("Dealer Busted, You Win");
            money += bet * bettingPower;
            cleanHands();
            return;
        } else if (dealerHandTotal == 21) {
            System.out.println("Dealer Black Jack, You Lose");
            cleanHands();
            return;
        }

        System.out.println("The dealer has: ");

        for (Card value : dealerHand) {
            System.out.print(value.value + " of " + value.suit + "\n");
        }

        System.out.println("Which totals to " + dealerHandTotal);

        if (userHandTotal > dealerHandTotal) {
            money += bet * bettingPower;
            System.out.println("You Win");
        }else {
            System.out.println("You lose");
        }
    }

    private static void printKnownCards(){

        int firstCard = 0;

        System.out.println("The dealer's known card is " + dealerHand.get(firstCard).value + " of "
                + dealerHand.get(firstCard).suit + "\n");

        System.out.println("Your current cards are: ");

        for (int card = 0; card < userHand.size(); card++) {
            System.out.print(userHand.get(card).value + " of " + userHand.get(card).suit + "\n");
        }

        System.out.println("That totals to " + userHandTotal + "\n");
    }

    private static void cleanHands(){

        for (Card card : userHand){
            userHand.remove(card);
        }

        for (Card card : dealerHand){
            dealerHand.remove(card);
        }
    }
}

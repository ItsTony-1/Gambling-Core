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
    public static ArrayList<Card> userSplitHand = new ArrayList<>();
    static int userHandTotal = 0;
    static int userSplitHandTotal = 0;
    static boolean userHasAce = false;
    static boolean splitHand = false;

    public static ArrayList <Card> dealerHand = new ArrayList<>();
    static int dealerHandTotal = 0;
    static boolean dealerHasAce = false;

    public BlackJack(){
        dataWriter.setPrettyPrinting();
        createNewDeck();
    }

    /**
     * Draws 2 cards and gives them to either the player or the dealer
     * <p> Calls: {@link Game#drawCards()} {@link BlackJack#parseCard(Card)}
     * @param toPlayer whether it goes to the player or dealer
     */
    public static void startPlaying(boolean toPlayer){

        Card drawnCard = drawCards();
        Card drawnCard2 = drawCards();

        if (toPlayer){
            userHand.add(drawnCard);
            userHand.add(drawnCard2);

            for (Card card : userHand) {

                int tempValue = parseCard(card);

                if (tempValue == 1){
                    tempValue += 10;
                    userHasAce = true;
                }
                userHandTotal += tempValue;
                if (userHasAce && userHandTotal > 21) {
                    userHandTotal -= 10;
                    userHasAce = false;
                }
            }

        }else {
            dealerHand.add(drawnCard);
            dealerHand.add(drawnCard2);

            for (Card card : dealerHand) {

                int tempValue = parseCard(card);

                if (tempValue == 1){
                    tempValue += 10;
                    dealerHasAce = true;
                }
                dealerHandTotal += tempValue;

                if (userHasAce && dealerHandTotal > 21) {
                    dealerHandTotal -= 10;
                    dealerHasAce = false;
                }
            }
        }
    }

    /**
     * gets user input on decisions of what to do, double, hit, stand, split
     * <p>
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
                System.out.println("Would you like to Hit[1], Stand[2], Double[3], or Split[4]?");
            } else if (isFirstAsk) {
                System.out.println("Would you like to Hit[1], Stand[2], or Double[3]?");
            } else {
                System.out.println("Would you like to Hit[1] or Stand[2]?");
            }

            String userInput = console.nextLine();
            int userInputParsed;

            try{
                userInputParsed = Integer.parseInt(userInput);
                if (userInputParsed < 1 || userInputParsed > 4){
                    System.out.println("Invalid input");
                    continue;
                }
            }catch (NumberFormatException Ex){
                System.out.println("Invalid input");
                continue;
            }

            boolean invalidInput = false;

            switch (userInputParsed){
                case 1 -> {
                    userHit();
                    switch (bustOrBJ(userHandTotal, userHasAce, true)){
                        case MatchEnd.BUST, MatchEnd.BLACKJACK -> {return;}
                    }
                }
                case 2 -> {
                    checkForWin();
                    cleanHands();
                }
                case 3 -> {
                    if (isFirstAsk && money >= bet){
                        userDouble();
                    }else {
                        invalidInput = true;
                        isFirstAsk = false;
                    }
                }
                case 4 -> {
                    if (isFirstAsk && duplicateCards){
                        userSplitHand();
                        splitHand = true;
                    }else {
                        invalidInput = true;
                    }
                }
                default -> invalidInput = true;
            }

            if (invalidInput){
                continue;
            }
            if (userHand.isEmpty() || dealerHand.isEmpty()){
                return;
            }
            if (isFirstAsk){
                isFirstAsk = false;
            }
        }
    }

    private static void userSplitHand() {

        Card newCard1 = drawCards();
        Card newCard2 = drawCards();

        userSplitHand.add(userHand.get(1));
        userHand.remove(1);

        userHand.add(newCard1);
        userSplitHand.add(newCard2);



        userHandTotal = 0;
        userSplitHandTotal = 0;

        for (Card card : userHand) {

            int tempValue = parseCard(card);

            if (tempValue == 1){
                tempValue += 10;
                userHasAce = true;
            }
            userHandTotal += tempValue;
            if (userHasAce && userHandTotal > 21) {
                userHandTotal -= 10;
                userHasAce = false;
            }
        }





    }

    private static boolean checkForDuplicateCards() {
        int card1;
        int card2;

        card1 = parseCard(userHand.get(0));
        card2 = parseCard(userHand.get(1));

        return card1 == card2;
    }

    private static int parseCard(Card card) {
        try {
            return Integer.parseInt(card.value);
        }catch (NumberFormatException ex){
            switch (card.value) {
                case "ACE" -> {
                    return  1;
                }
                case "JACK", "QUEEN", "KING" -> {
                    return  10;
                }
                default -> {
                    System.out.println("ERROR DATA FAILED 1");
                    return -99999;
                }
            }
        }
    }

    private static MatchEnd bustOrBJ(int handTotal, boolean hasAce, boolean isPlayer) {

        if (handTotal > 21){
            if (hasAce){
                if (isPlayer) {
                    userHandTotal -= 10;
                }else {
                    dealerHandTotal -= 10;
                }
                handTotal -= 10;
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
        userHandTotal += parseCard(drawnCard);

        System.out.println("You drew a " + drawnCard.value + " of " + drawnCard.suit);
    }

    private static void userDouble() {
        userHit();

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
            dealerHandTotal += parseCard(dealerCard);
            dealerHand.add(dealerCard);
        }

        printDealerCards();
        switch (bustOrBJ(dealerHandTotal, dealerHasAce, false)){
            case MatchEnd.BUST -> {
                money += bet * bettingPower;
                return;
            }
            case MatchEnd.BLACKJACK -> {
                return;
            }
        }

        if (userHandTotal > dealerHandTotal) {
            money += bet * bettingPower;
            System.out.println("You Win");
        }else {
            System.out.println("You lose");
        }
    }

    private static void printDealerCards() {
        System.out.println("The dealer has: ");

        for (Card value : dealerHand) {
            System.out.print(value.value + " of " + value.suit + "\n");
        }

        System.out.println("Which totals to " + dealerHandTotal);
    }

    private static void printKnownCards(){

        int firstCard = 0;

        System.out.println("The dealer's known card is " + dealerHand.get(firstCard).value + " of "
                + dealerHand.get(firstCard).suit + "\n");

        System.out.println("Your current cards are: ");

        for (Card value : userHand) {
            System.out.print(value.value + " of " + value.suit + "\n");
        }

        System.out.println("That totals to " + userHandTotal + "\n");
    }

    private static void cleanHands(){

        if (!userHand.isEmpty()) {
            userHand.subList(0, userHand.size()).clear();
        }

        if (!dealerHand.isEmpty()) {
            dealerHand.subList(0, dealerHand.size()).clear();
        }
    }
}

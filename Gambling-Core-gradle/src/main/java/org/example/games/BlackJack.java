package org.example.games;

import org.example.Card;

import javax.swing.*;
import java.util.Scanner;

public class BlackJack extends Game{

    enum MatchEnd {IGNORE, BUST, BLACKJACK}

    public static double money = 2500.00;
    public static double bettingPower = 2;
    public static double bet = 0;

    static Scanner console = new Scanner(System.in);



    public static Hand userHand = new Hand(false, "Main Hand");
    public static Hand userSplitHand = new Hand(true, "Secondary Hand");
    public static Hand dealerHand = new Hand(false, "Dealer Hand");

    public static Hand currentHand = userHand;

    public BlackJack(){
        dataWriter.setPrettyPrinting();
        createNewDeck();
        createBlackjackScreen();
    }

    /**
     * Draws 2 cards and gives them to either the player or the dealer
     * <p> Calls: {@link Game#drawCards()} {@link BlackJack#loopThroughHand(Hand)}
     * @param toPlayer whether it goes to the player or dealer
     */
    public static void startPlaying(boolean toPlayer){

        Card drawnCard = drawCards();
        Card drawnCard2 = drawCards();

        if (toPlayer){
            userHand.add(drawnCard);
            userHand.add(drawnCard2);

            loopThroughHand(userHand);
        }else {
            dealerHand.add(drawnCard);
            dealerHand.add(drawnCard2);

            loopThroughHand(dealerHand);
        }
    }

    /**
     * Pre-Game Betting & determines an early win
     * <p>
     * Calls: {@link BlackJack#printKnownCards(Hand)} {@link BlackJack#startBetting()}
     * {@link BlackJack#bustOrBJ(Hand, boolean)} {@link BlackJack#playGame(Hand, Boolean)}
     */
    public static void getUserInput(){
        printKnownCards(userHand);
        startBetting();

        switch (bustOrBJ(userHand, true)){
            case MatchEnd.BUST, MatchEnd.BLACKJACK -> {return;}
        }

        switch (bustOrBJ(dealerHand, false)){
            case MatchEnd.BUST, MatchEnd.BLACKJACK -> {return;}
        }

        playGame(userHand, true);
    }

    /**
     * Gets user input on decisions of what to do, double, hit, stand, split
     * <p> Calls: {@link BlackJack#checkForDuplicateCards(Hand)} {@link BlackJack#printKnownCards(Hand)}
     * {@link BlackJack#userHit(Hand)} {@link BlackJack#userDouble()} {@link BlackJack#userSplitHand()}
     * {@link BlackJack#stand(Hand)}
     * @param hand The list of cards allocated to either the user or dealer
     */
    private static void playGame(Hand hand, Boolean isFirstAsk) {

        boolean duplicateCards = checkForDuplicateCards(hand);

        while(true) {
            printKnownCards(hand);

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
                    userHit(hand);
                    switch (bustOrBJ(hand, true)){
                        case MatchEnd.BUST, MatchEnd.BLACKJACK -> {return;}
                    }
                }
                case 2 -> {
                    stand(hand);
                    return;
                }
                case 3 -> {
                    if (isFirstAsk && money >= bet){
                        userDouble();
                        return;
                    }else {
                        System.out.print("Invalid Input, ");

                        if (!isFirstAsk){
                            System.out.println("Not available");
                        }else {
                            System.out.println("Not enough Money");
                        }

                        invalidInput = true;
                        isFirstAsk = false;
                    }
                }
                case 4 -> {
                    if (isFirstAsk && duplicateCards){
                        userSplitHand();
                        userHand.handSplit = true;
                        return;
                    }else {

                        System.out.print("Invalid Input, ");

                        if (!isFirstAsk){
                            System.out.println("Not available");
                        }else {
                            System.out.println("You Don't have duplicate cards");
                        }

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

    /**
     * Ends the hand's dialogue
     * <p>Calls: {@link BlackJack#checkForWin(Hand)} {@link BlackJack#cleanHands()}
     * @param hand Hand that gets checked
     */
    private static void stand(Hand hand) {
        if (hand.handSplit){
            return;
        }
        checkForWin(hand);
        cleanHands();
    }

    /**
     * Splits the user's hand into 2 separate hands, has the user play the game with both hands
     * <p> Calls: {@link Game#drawCards()} {@link BlackJack#loopThroughHand(Hand)}
     * {@link BlackJack#playGame(Hand, Boolean)} {@link BlackJack#checkForWin(Hand)} {@link BlackJack#cleanHands()}
     */
    private static void userSplitHand() {

        Card newCard1 = drawCards();
        Card newCard2 = drawCards();

        userHand.handSplit = true;

        userSplitHand.add(userHand.remove(1));

        userHand.add(newCard1);
        userSplitHand.add(newCard2);

        userHand.total = 0;
        userSplitHand.total = 0;

        loopThroughHand(userHand);
        currentHand = userSplitHand;
        loopThroughHand(userSplitHand);

        playGame(userHand, false);
        playGame(userSplitHand, false);

        checkForWin(userHand);
        checkForWin(userSplitHand);

        cleanHands();
    }

    /**
     * Loops through a list of cards, totals the value of those card
     * <p> Calls: {@link BlackJack#parseCard(Card)}
     * @param hand That gets looped through
     */
    private static void loopThroughHand(Hand hand) {
        for (Card card : hand) {

            int tempValue = parseCard(card);

            if (tempValue == 1){
                tempValue += 10;
                hand.hasAce = true;
            }
            hand.total += tempValue;
            if (hand.hasAce && hand.total > 21) {
                hand.total -= 10;
                hand.hasAce = false;
            }
        }
    }

    /**
     * Checks to see if the hands starting cards are the same
     * @param hand Hand to check
     * @return true if card1 is the same as card2
     */
    private static boolean checkForDuplicateCards(Hand hand) {
        int card1;
        int card2;

        card1 = parseCard(hand.get(0));
        card2 = parseCard(hand.get(1));

        return card1 == card2;
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

    /**
     * Checks to see if a hand Busts or gets a Black Jack
     * @param hand to check
     * @param isPlayer if the hand being Checked is the Player's hand
     * @return {@link MatchEnd}
     */
    private static MatchEnd bustOrBJ(Hand hand, boolean isPlayer) {

        if (hand.total > 21){
            if (hand.hasAce){
                hand.total -= 10;
                hand.hasAce = false;
            }else {
                if (isPlayer) {
                    System.out.println("Busted, You Lose");
                } else {
                    System.out.println("Dealer Busted, You Win");
                    money += bet * bettingPower;
                }
                if (!hand.handSplit) {
                    cleanHands();
                }
                return MatchEnd.BUST;
            }
        }

        if (hand.total == 21) {
            if (isPlayer) {
                System.out.println("Black Jack, You Win");
                money += bet * bettingPower;
            }else {
                System.out.println("Dealer Black Jack, Dealer Win");
            }
            if (!hand.handSplit) {
                cleanHands();
            }
            return MatchEnd.BLACKJACK;
        }

        return MatchEnd.IGNORE;
    }

    /**
     * Adds card to a hand & adds the value of that card to the total
     * @param hand Hand to add card to
     */
    private static void userHit(Hand hand) {
        Card drawnCard = drawCards();
        hand.add(drawnCard);
        hand.total += parseCard(drawnCard);

        System.out.println("You drew a " + drawnCard.value + " of " + drawnCard.suit);
    }

    /**
     * Doubles bet, draws a card then checks for win
     * <p> Calls: {@link BlackJack#bustOrBJ(Hand, boolean)} {@link BlackJack#userHit(Hand)}
     * {@link BlackJack#checkForWin(Hand)} {@link BlackJack#cleanHands()}
     */
    private static void userDouble() {

        money -= bet;
        bet += bet;

        userHit(userHand);

        switch (bustOrBJ(userHand, true)){
            case MatchEnd.BUST -> {return;}
            case MatchEnd.BLACKJACK -> {
                money += bet * bettingPower;
                return;
            }
        }

        if (!userHand.handSplit) {
            checkForWin(userHand);
            cleanHands();
        }
    }

    /**
     * Gets input from user how much they want to bet, must be greater than $5
     */
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
                if (bet >= 5) {
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

    /**
     * Checks for win for a hand
     * <p>If dealer has < 17 in total cardValue, dealer hits</p>
     * Calls: {@link BlackJack#printDealerCards()} {@link BlackJack#bustOrBJ(Hand, boolean)}
     * @param hand Hand that win is Checked for
     */
    private static void checkForWin(Hand hand) {
        while (dealerHand.total < 17) {
            Card dealerCard = drawCards();
            dealerHand.total += parseCard(dealerCard);
            dealerHand.add(dealerCard);
        }

        if (hand.handSplit && !hand.handName.equals(userSplitHand.handName)) {
            printDealerCards();
        }
        switch (bustOrBJ(dealerHand, false)){
            case MatchEnd.BUST -> {
                money += bet * bettingPower;
                return;
            }
            case MatchEnd.BLACKJACK -> {
                return;
            }
        }

        if (hand.total > dealerHand.total) {
            money += bet * bettingPower;
            System.out.println(hand.handName + " Wins");
        }else {
            System.out.println(hand.handName + " loses");
        }
    }

    /**
     * Prints the dealer's cards
     */
    private static void printDealerCards() {
        System.out.println("The dealer has: ");

        for (Card value : dealerHand) {
            System.out.print(value.value + " of " + value.suit + "\n");
        }

        System.out.println("Which totals to " + dealerHand.total);
    }

    /**
     * Prints known dealer Card & Hand user is currently playing with
     * @param hand User hand currently in use
     */
    private static void printKnownCards(Hand hand){

        int firstCard = 0;

        System.out.println("The dealer's known card is " + dealerHand.get(firstCard).value + " of "
                + dealerHand.get(firstCard).suit + "\n");

        System.out.println("Your current cards in your " + hand.handName + " are: ");

        for (Card value : hand) {
            System.out.print(value.value + " of " + value.suit + "\n");
        }

        System.out.println("That totals to " + hand.total + "\n");

    }

    /**
     * Removes all cards from all hands
     */
    private static void cleanHands(){

        if (!userHand.isEmpty()) {
            userHand.subList(0, userHand.size()).clear();
            userHand.total = 0;
        }

        if (!userSplitHand.isEmpty()){
            userSplitHand.subList(0,userSplitHand.size()).clear();
            userSplitHand.total = 0;
        }

        if (!dealerHand.isEmpty()) {
            dealerHand.subList(0, dealerHand.size()).clear();
            dealerHand.total = 0;
        }
    }

    /**
     * Creates the GUI of the BlackJack table
     */
    public void createBlackjackScreen(){

        this.setVisible(true);

        JPanel dealerHand = new JPanel();
        JPanel userHand1 = new JPanel();
        JPanel userHand2 = new JPanel();
        JPanel betting = new JPanel();
        JPanel options = new JPanel();

        dealerHand.setVisible(true);
        userHand1.setVisible(true);
        userHand2.setVisible(true);
        betting.setVisible(true);
        options.setVisible(true);

        JSplitPane userDealerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane dealerBettingSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane userHandSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane gameOptionsSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        userDealerSplit.setVisible(true);
        dealerBettingSplit.setVisible(true);
        userHandSplit.setVisible(true);
        gameOptionsSplit.setVisible(true);

        gameOptionsSplit.setDividerLocation(1920-1920/3);

        this.add(gameOptionsSplit);
        gameOptionsSplit.setRightComponent(options);
        gameOptionsSplit.setLeftComponent(userDealerSplit);

        userDealerSplit.setTopComponent(dealerBettingSplit);
        dealerBettingSplit.setLeftComponent(dealerHand);
        dealerBettingSplit.setRightComponent(betting);

        userDealerSplit.setBottomComponent(userHandSplit);
        userHandSplit.setLeftComponent(userHand1);
        userDealerSplit.setRightComponent(userHand2);

        JButton hit = new JButton("Hit");
        hit.setVisible(true);
        hit.addActionListener(e -> {
            userHit(currentHand);
        });

        JButton stand = new JButton("Stand");

        JButton doubleB = new JButton("double");

        JButton splitHand = new JButton("Split hand");


        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        options.add(hit);
        options.add(stand);
        options.add(doubleB);
        options.add(splitHand);

    }
}

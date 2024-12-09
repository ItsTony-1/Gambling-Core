package org.example.games;

import org.example.Card;
import org.example.Popup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class BlackJack extends Game{

    enum MatchEnd {IGNORE, BUST, BLACKJACK}

    public static double money = 2500.00;
    public static double bettingPower = 2;
    public static double bet = 0;

    static Scanner console = new Scanner(System.in);

    static JPanel dealerHandPanel = new JPanel();
    static JPanel userHand1 = new JPanel();
    static JPanel userHand2 = new JPanel();
    static JPanel betting = new JPanel();
    static JPanel options = new JPanel();

    static JButton hit = new JButton("Hit");
    static JButton stand = new JButton("Stand");
    static JButton doubleButton = new JButton("double");
    static JButton splitHand = new JButton("Split hand");
    static JButton confirmBet = new JButton("Confirm Bet");

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

    private static MatchEnd bustOrBJ(Hand hand) {

        if (hand.total > 21){
            if (hand.hasAce){
                hand.total -= 10;
                hand.hasAce = false;
            }else {
                return MatchEnd.BUST;
            }
        }

        if (hand.total == 21) {
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

    private static Image userHitUI(Hand hand) {
        Card drawnCard = drawCards();
        hand.add(drawnCard);
        hand.total += parseCard(drawnCard);

        return drawnCard.getCardImage();
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

        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(1440, 810));
        this.setVisible(true);
        this.setName("BlackJack");


        //region Panel set up
        dealerHandPanel.setVisible(true);
        userHand1.setVisible(true);
        userHand2.setVisible(true);
        betting.setVisible(true);
        options.setVisible(true);
        //endregion

        //region Split pane set up
        JSplitPane userDealerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane dealerBettingSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane userHandSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane gameOptionsSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        userDealerSplit.setVisible(true);
        dealerBettingSplit.setVisible(true);
        userHandSplit.setVisible(true);
        gameOptionsSplit.setVisible(true);

        gameOptionsSplit.setDividerLocation(1440-1440/3);
        gameOptionsSplit.setPreferredSize(new Dimension(1440, 810));

        this.add(gameOptionsSplit);
        gameOptionsSplit.setRightComponent(dealerBettingSplit);
        gameOptionsSplit.setLeftComponent(userDealerSplit);

        dealerBettingSplit.setDividerLocation(400);
        dealerBettingSplit.setTopComponent(betting);
        dealerBettingSplit.setBottomComponent(options);

        userDealerSplit.setDividerLocation(400);
        userDealerSplit.setTopComponent(dealerHandPanel);


        userDealerSplit.setBottomComponent(userHandSplit);

        userHandSplit.setDividerLocation(1440-2*(1440/3));
        userHandSplit.setLeftComponent(userHand1);
        userHandSplit.setRightComponent(userHand2);
        //endregion

        setUpBettingScreen();
        setUpOptionsMenu();

    }

    private void repaintRevalidate() {
        options.repaint();
        options.revalidate();
        betting.repaint();
        betting.revalidate();
        dealerHandPanel.repaint();
        dealerHandPanel.revalidate();
        userHand1.repaint();
        userHand1.revalidate();
        userHand2.repaint();
        userHand2.revalidate();
        this.repaint();
        this.revalidate();
    }

    private void setUpBettingScreen() {
        JLabel betHereTB = new JLabel("Place your Bet To Begin");
        betHereTB.setVisible(true);

        JLabel minBet = new JLabel("Minimum bet is $5");
        minBet.setVisible(true);

        JTextField betBox = new JTextField();
        betBox.setVisible(true);

        confirmBet.setVisible(true);

        JLabel moneyLabel = new JLabel("$" + money);
        moneyLabel.setVisible(true);

        betting.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 20;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        betting.add(betHereTB, constraints);

        constraints.ipady = 10;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 1;
        betting.add(minBet,constraints);

        constraints.ipady = 20;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 2;
        betting.add(betBox, constraints);

        constraints.ipady = 10;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.gridx = 2;
        constraints.gridy = 3;
        betting.add(confirmBet, constraints);

        constraints.ipady = 10;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 3;
        betting.add(moneyLabel, constraints);

        confirmBet.addActionListener(e -> {
            try{
                bet = Integer.parseInt(betBox.getText());
                money -= bet;
                confirmBet.setVisible(false);
                betHereTB.setText("Your Current Bet is: " + bet);

                startPlaying(true);

                for (Card card : userHand) {
                    userHand1.add(new JLabel(new ImageIcon(card.getCardImage())));
                }

                startPlaying(false);
                dealerHandPanel.add(new JLabel(new ImageIcon (dealerHand.getFirst().getCardImage())));
                dealerHandPanel.add(new JLabel(new ImageIcon (dealerHand.getFirst().getCardBack())));

                userHand1.repaint();
                userHand1.revalidate();

                dealerHandPanel.repaint();
                dealerHandPanel.revalidate();

            } catch (NumberFormatException ex){
                betHereTB.setText("ERROR INCORRECT INPUT");
                confirmBet.setVisible(true);
            }

            hit.setVisible(true);
            stand.setVisible(true);
            doubleButton.setVisible(true);
            splitHand.setVisible(true);
        });
    }

    private void setUpOptionsMenu() {
        hit.addActionListener(e -> {
            Image cardImage = userHitUI(currentHand);
            if (userHand == currentHand){
                userHand1.add(new JLabel(new ImageIcon(cardImage)));
                repaintRevalidate();
            }else {
                userHand2.add(new JLabel(new ImageIcon(cardImage)));
                repaintRevalidate();
            }

            switch (bustOrBJ(userHand)) {
                case BLACKJACK -> {
                    Popup winBlackJack = new Popup(true);
                    winBlackJack.setWinCondition("You got a total of 21! That's a Black Jack!");
                    winBlackJack.confirm.addActionListener(ee -> {

                        if (!currentHand.handSplit) {
                            cleanHands();
                            userHand1.removeAll();
                            userHand2.removeAll();
                            dealerHandPanel.removeAll();
                            confirmBet.setVisible(true);

                            repaintRevalidate();
                        }else {
                            currentHand = userSplitHand;
                        }

                        winBlackJack.dispatchEvent(new WindowEvent(winBlackJack,
                                WindowEvent.WINDOW_CLOSING));
                    });

                    hit.setVisible(false);
                    stand.setVisible(false);

                    bet *= bettingPower;
                }
                case BUST -> {
                    Popup loseBusted = new Popup(false);
                    loseBusted.setWinCondition("You went over 21, you lost");
                    loseBusted.confirm.addActionListener(ee -> {

                        if (!currentHand.handSplit) {
                            cleanHands();
                            userHand1.removeAll();
                            userHand2.removeAll();
                            dealerHandPanel.removeAll();
                            confirmBet.setVisible(true);

                            repaintRevalidate();
                        }

                        loseBusted.dispatchEvent(new WindowEvent(loseBusted, WindowEvent.WINDOW_CLOSING));
                    });
                    hit.setVisible(false);
                    stand.setVisible(false);
                    repaintRevalidate();
                }
            }

            doubleButton.setVisible(false);
            splitHand.setVisible(false);
            repaintRevalidate();
        });

        stand.addActionListener(e -> {
            standUI();
        });

        doubleButton.addActionListener(e -> {
            money -= bet;
            bet += bet;

            userHand1.add(new JLabel(new ImageIcon(userHitUI(userHand))));
            repaintRevalidate();

            switch (bustOrBJ(userHand)){
                case BLACKJACK -> {
                    Popup winBlackJack = new Popup(true);
                    winBlackJack.setWinCondition("You got a total of 21! That's a Black Jack!");
                    winBlackJack.confirm.addActionListener( ee -> {

                        cleanHands();
                        userHand1.removeAll();
                        userHand2.removeAll();
                        dealerHandPanel.removeAll();
                        confirmBet.setVisible(true);

                        repaintRevalidate();

                        winBlackJack.dispatchEvent(new WindowEvent(winBlackJack,
                                WindowEvent.WINDOW_CLOSING));
                    });

                    hit.setVisible(false);
                    stand.setVisible(false);
                    doubleButton.setVisible(false);

                    repaintRevalidate();

                    money = bet * bettingPower;
                    bet = 0;
                }
                case BUST -> {
                    Popup loseBusted = new Popup(false);
                    loseBusted.setWinCondition("You went over 21, you lost");
                    loseBusted.confirm.addActionListener( ee -> {

                        if (!currentHand.handSplit){
                            cleanHands();
                            userHand1.removeAll();
                            userHand2.removeAll();
                            dealerHandPanel.removeAll();
                            confirmBet.setVisible(true);

                            repaintRevalidate();
                        }

                        loseBusted.dispatchEvent(new WindowEvent(loseBusted, WindowEvent.WINDOW_CLOSING));
                    });
                    hit.setVisible(false);
                    stand.setVisible(false);
                }
            }

            standUI();
        });

        splitHand.addActionListener( e -> {

            Card newCard1 = drawCards();
            Card newCard2 = drawCards();

            userHand.handSplit = true;

            userSplitHand.add(userHand.remove(1));

            userHand.add(newCard1);
            userSplitHand.add(newCard2);

            userHand.total = 0;
            userSplitHand.total = 0;

            loopThroughHand(userHand);
            loopThroughHand(userSplitHand);
        });

        //region options buttons Pt 2
        options.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 20;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        options.add(hit, constraints);

        constraints.gridy = 1;
        options.add(stand, constraints);

        constraints.gridy = 2;
        options.add(doubleButton, constraints);

        constraints.gridy = 3;
        options.add(splitHand,constraints);

        hit.setVisible(false);
        stand.setVisible(false);
        doubleButton.setVisible(false);
        splitHand.setVisible(false);
    }

    private void standUI() {
        if (currentHand.handSplit){
            currentHand = userSplitHand;
            return;
        }

        stand.setVisible(false);
        hit.setVisible(false);

        dealerHandPanel.removeAll();
        dealerHandPanel.add(new JLabel(new ImageIcon(dealerHand.getFirst().getCardImage())));
        dealerHandPanel.add(new JLabel(new ImageIcon(dealerHand.getLast().getCardImage())));
        repaintRevalidate();

        while (dealerHand.total < 17) {
            Card dealerCard = drawCards();
            dealerHand.total += parseCard(dealerCard);
            dealerHand.add(dealerCard);

            dealerHandPanel.add(new JLabel(new ImageIcon(dealerCard.getCardImage())));
            repaintRevalidate();
        }


        switch (bustOrBJ(dealerHand)){
            case MatchEnd.BUST -> {
                money += bet * bettingPower;

                Popup winBusted = new Popup(true);
                winBusted.setWinCondition("Dealer went over 21");
                winBusted.confirm.addActionListener( ee -> {

                    cleanHands();
                    userHand1.removeAll();
                    userHand2.removeAll();
                    dealerHandPanel.removeAll();
                    confirmBet.setVisible(true);

                    repaintRevalidate();

                    winBusted.dispatchEvent(new WindowEvent(winBusted, WindowEvent.WINDOW_CLOSING));
                });

                hit.setVisible(false);
                stand.setVisible(false);
                return;
            }
            case MatchEnd.BLACKJACK -> {
                Popup dealerWinBlackJack = new Popup(false);
                dealerWinBlackJack.setWinCondition("The dealer got a total of 21." +
                        " That's a Black Jack!");
                dealerWinBlackJack.confirm.addActionListener(ee -> {

                    if (!currentHand.handSplit){
                        cleanHands();
                        userHand1.removeAll();
                        userHand2.removeAll();
                        dealerHandPanel.removeAll();
                        confirmBet.setVisible(true);

                        repaintRevalidate();
                    }

                    dealerWinBlackJack.dispatchEvent(new WindowEvent(dealerWinBlackJack,
                            WindowEvent.WINDOW_CLOSING));
                });
                hit.setVisible(false);
                stand.setVisible(false);
                return;
            }
        }

        int countOfUserHand = 2;
        for (int setCurrentHand = 0; setCurrentHand < countOfUserHand; setCurrentHand++) {
            switch (setCurrentHand){
                case 0 -> currentHand = userHand;
                case 1 -> currentHand = userSplitHand;
            }

            if (!currentHand.isEmpty()) {
                if (dealerHand.total > currentHand.total) {

                    Popup dealerWinLargerTotal = new Popup(false);
                    dealerWinLargerTotal.setWinCondition("The dealer got a total of " + dealerHand.total +
                            ". That is larger than your " + currentHand + ".");
                    dealerWinLargerTotal.confirm.addActionListener(ee -> {

                        if (!currentHand.handSplit) {
                            cleanHands();
                            userHand1.removeAll();
                            userHand2.removeAll();
                            dealerHandPanel.removeAll();
                            confirmBet.setVisible(true);

                            repaintRevalidate();
                        }

                        dealerWinLargerTotal.dispatchEvent(new WindowEvent(dealerWinLargerTotal,
                                WindowEvent.WINDOW_CLOSING));
                    });
                    hit.setVisible(false);
                    stand.setVisible(false);
                }else {
                    money += bet * bettingPower;

                    Popup userWinLargerTotal = new Popup(true);
                    userWinLargerTotal.setWinCondition("The dealer got a total of " + dealerHand.total +
                            ". That is smaller than your " + currentHand + ".");
                    userWinLargerTotal.confirm.addActionListener(ee -> {

                        if (!currentHand.handSplit) {
                            cleanHands();
                            userHand1.removeAll();
                            userHand2.removeAll();
                            dealerHandPanel.removeAll();
                            confirmBet.setVisible(true);
                            repaintRevalidate();
                        }

                        userWinLargerTotal.dispatchEvent(new WindowEvent(userWinLargerTotal,
                                WindowEvent.WINDOW_CLOSING));
                    });
                }
            }
        }
    }
}
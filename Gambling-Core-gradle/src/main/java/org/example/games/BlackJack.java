package org.example.games;

import org.example.Card;

import java.util.ArrayList;
import java.util.Scanner;

public class BlackJack extends Game{

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

    public static void startPlaying(boolean toPlayer){

        Card drawnCard = drawCards();
        Card drawnCard2 = drawCards();

        if (toPlayer){
            userHand.add(drawnCard);
            userHand.add(drawnCard2);

            for (int card = 0; card < userHand.size(); card++){
                try {
                    int cardValue = Integer.parseInt(userHand.get(card).value);
                    if (cardValue == 1){
                        cardValue+= 10;
                    }
                    userHandTotal += cardValue;
                }catch (NumberFormatException ex){
                    switch (userHand.get(card).value){
                        case "JACK" -> userHandTotal += 10;
                        case "QUEEN" -> userHandTotal += 10;
                        case "KING" -> userHandTotal += 10;
                        default -> System.out.println("ERROR DATA FAILED 1");
                    }
                }
            }

            if (userHandTotal == 21){
                System.out.println("Black Jack, You win");
            }

        }else {
            dealerHand.add(drawnCard);
            dealerHand.add(drawnCard2);

            for (int card = 0; card < dealerHand.size(); card++){
                try {
                    dealerHandTotal += Integer.parseInt(dealerHand.get(card).value);
                }catch (NumberFormatException ex){
                    switch (dealerHand.get(card).value){
                        case "JACK" -> dealerHandTotal += 10;
                        case "QUEEN" -> dealerHandTotal += 10;
                        case "KING" -> dealerHandTotal += 10;
                        default -> System.out.println("ERROR DATA FAILED 1");
                    }
                }
            }
        }
    }

    public static void getUserInput(){

        boolean isFirstAsk = true;
        boolean playing = true;

        printKnownCards();

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
                money -= bet;
                break;
            } catch (NumberFormatException ex) {
                System.out.println("NOT A CASH VALUE TRY AGAIN");
            }
        }

        while(playing) {
            printKnownCards();

            if (isFirstAsk){
                System.out.println("Would you like to hit, stand, or double?");
            }else {
                System.out.println("Would you like to hit or stand?");
            }

            String userInput = console.nextLine();

            if (userInput.equals("double") || userInput.equals("hit") || userInput.equals("stand")){
                boolean verify = true;
            }else {
                System.out.println("Invalid input");
                continue;
            }

            Card drawnCard = drawCards();

            if (isFirstAsk){
                if (userInput.equals("double")){
                    userHand.add(drawnCard);
                    try {
                        userHandTotal += Integer.parseInt(drawnCard.value);
                    } catch (NumberFormatException ex){
                        switch (drawnCard.value){
                            case "JACK" -> userHandTotal += 10;
                            case "QUEEN" -> userHandTotal += 10;
                            case "KING" -> userHandTotal += 10;
                            default -> System.out.println("ERROR DATA FAILED 3");
                        }

                        System.out.println("You drew a " + drawnCard.value + " of " + drawnCard.suit);

                        if (userHandTotal > 21){
                            System.out.println("Busted, You Lose");
                            break;
                        }
                    }

                    money -= bet;
                    bet = bet*2;

                    checkForWin();
                }
            }else {
                if (userInput.equals("double")) {
                    System.out.println("Invalid input");
                    continue;
                }
            }

            if (userInput.equals("hit")){
                userHand.add(drawnCard);
                try {
                    userHandTotal += Integer.parseInt(drawnCard.value);
                } catch (NumberFormatException ex){
                    switch (drawnCard.value){
                        case "JACK" -> userHandTotal += 10;
                        case "QUEEN" -> userHandTotal += 10;
                        case "KING" -> userHandTotal += 10;
                        default -> System.out.println("ERROR DATA FAILED 4");
                    }
                }

                System.out.println("You drew a " + drawnCard.value + " of " + drawnCard.suit);

                if (userHandTotal > 21){
                    System.out.println("Busted, You Lose");
                    break;
                } else if (userHandTotal == 21) {
                    System.out.println("Black Jack, You Win");
                    money = bet * 2;
                    break;
                }

            } else if (userInput.equals("stand")) {

                while (dealerHandTotal < 17) {
                    Card dealerCard = drawCards();
                    try {
                        dealerHandTotal += Integer.parseInt(dealerCard.value);
                    } catch (NumberFormatException ex){
                        switch (drawnCard.value){
                            case "JACK" -> dealerHandTotal += 10;
                            case "QUEEN" -> dealerHandTotal += 10;
                            case "KING" -> dealerHandTotal += 10;
                            default -> System.out.println("ERROR DATA FAILED 5");
                        }
                    }

                    dealerHand.add(dealerCard);

                    if (dealerHandTotal > 21){
                        System.out.println("Dealer Busted, You Win");
                        money = bet * 2;
                        break;
                    } else if (dealerHandTotal == 21) {
                        System.out.println("Dealer Black Jack, You Lose");
                    }
                }

                checkForWin();
                bet = 0;
                break;
            }

            if (isFirstAsk){
                isFirstAsk = false;
            }
        }
    }

    private static void checkForWin() {
        System.out.println("The dealer has: ");

        for (int card = 0; card < dealerHand.size(); card++) {
            System.out.print(dealerHand.get(card).value + " of " + dealerHand.get(card).suit + "\n");
        }

        System.out.println("Which totals to " + dealerHandTotal);

        if (userHandTotal > dealerHandTotal) {
            money = bet * 2;
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

}

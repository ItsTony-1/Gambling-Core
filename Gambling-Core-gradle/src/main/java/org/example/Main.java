package org.example;

import org.example.games.BlackJack;
import org.example.games.Game;

import java.util.Scanner;

public class Main {

    static Scanner console = new Scanner(System.in);
    static BlackJack blackJack = new BlackJack();

    public static void main(String[] args) {

        boolean gameSelected = false;
        String userInput;

        while (!gameSelected) {
            System.out.println("What game would you like to play? (Blackjack or Texas Hold Em");

            userInput ="Blackjack";// console.nextLine();

            switch (userInput){
                case "Blackjack" -> {
                    gameSelected = true;
                    playBlackJack();
                }
                case "Texas Hold Em" -> {
                    gameSelected = true;
                    playTexasHolEm();
                }
                default -> System.out.println("Not a game. Please choose a game or double check spelling");
            }
        }
    }

    private static void playTexasHolEm() {

    }

    private static void playBlackJack() {

        boolean playing = true;

        while(playing){

            blackJack.startPlaying(true);
            blackJack.startPlaying(false);

            blackJack.getUserInput();

            System.out.println("Would you like to play again? [Y]/[N]");
            while(true) {
                String userInput = console.nextLine();

                if (userInput.equalsIgnoreCase("no") ||
                        userInput.equalsIgnoreCase("n")){
                    playing = false;
                    break;
                } else if (blackJack.money <= 4) {
                    System.out.println("DAMN YOU BROKE, GET AWAY FROM OUR TABLE");
                    playing = false;
                    break;
                } else if (userInput.equalsIgnoreCase("yes") ||
                            userInput.equalsIgnoreCase("y")) {
                    break;
                }

                System.out.println("Invalid input");
            }

            if (Double.parseDouble(Game.deck.remaining) < Math.round(Double.parseDouble(Game.deck.remaining) * 0.60)){
                Game.shuffleDeck();
            }
        }
        if (blackJack.money > 2500){
            System.out.println("You left with $" + blackJack.money + ". That is a gain of $" +
                    (blackJack.money - 2500));
        } else if (blackJack.money < 2500) {
            System.out.println("You left with $" + blackJack.money + ". That is a loss of $" +
                    (2500 - blackJack.money));
        }
    }
}
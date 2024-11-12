package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.games.BlackJack;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //region texas hold 'em
    public static ArrayList <Card> theRiver = new ArrayList<>();
    //endRegion

    static Scanner console = new Scanner(System.in);
    static BlackJack blackJack = new BlackJack();

    public static void main(String[] args) {

        boolean gameSelected = false;
        boolean gameIsBlackJack = false;
        String userInput;

        while (!gameSelected) {
            System.out.println("What game would you like to play? (Blackjack or Texas Hold Em");

            userInput ="Blackjack";// console.nextLine();

            switch (userInput){
                case "Blackjack" -> {
                    gameSelected = true;
                    gameIsBlackJack = true;
                }
                case "Texas Hold Em" -> {
                    gameSelected = true;
                    gameIsBlackJack = false;
                }
                default -> System.out.println("Not a game. Please choose a game or double check spelling");
            }

            if (gameSelected){
                if (gameIsBlackJack){
                    playBlackJack();
                }else {
                    playTexasHolEm();
                }
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

            System.out.println("Would you like to play again?");
            while(true) {
                String userInput = console.nextLine();

                if (userInput.equals("yes") || userInput.equals("no")){
                    boolean verify = true;
                }else continue;

                if (userInput.equals("no")){
                    playing = false;
                } else if (blackJack.money <= 0) {
                    playing = false;
                } else if (userInput.equals("yes")) {
                    break;
                }
            }

            if (Double.parseDouble(blackJack.deck.remaining) < Math.round(Double.parseDouble(blackJack.deck.remaining) * 0.60)){
                blackJack.shuffleDeck();
            }
        }
        if (blackJack.money > 2500){
            System.out.println("You left with $" + blackJack.money + ". Thats a gain of $" +
                    (blackJack.money - 2500));
        } else if (blackJack.money < 2500) {
            System.out.println("You left with $" + blackJack.money + ". Thats a loss of $" +
                    (2500 - blackJack.money));
        }



    }


}
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

    public static void main(String[] args) {

        boolean gameSelected = false;
        String userInput;

        while (!gameSelected) {
            System.out.println("What game would you like to play? (Blackjack or Texas Hold Em");

            userInput = console.nextLine();

            switch (userInput){
                case "Blackjack" -> gameSelected = true;
                case "Texas Hold Em" -> gameSelected = true;
                default -> System.out.println("Choose a game or double check spelling");
            }
        }

    }

}
package org.example.games;

import org.example.Card;

import java.util.ArrayList;

public class BlackJack extends Game{

    public static double money = 2500.00;
    public static double bettingPower = 2;

    public static ArrayList<Card> userHand = new ArrayList<>();
    public static ArrayList <Card> dealerHand = new ArrayList<>();

    public BlackJack(){

        dataWriter.setPrettyPrinting();

        createNewDeck();


    }

}

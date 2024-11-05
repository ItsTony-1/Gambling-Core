package org.example.games;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Card;
import org.example.Deck;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Game {

    public static Deck deck;
    public static GsonBuilder dataWriter = new GsonBuilder();

    public Game(){}

    static void createNewDeck() {


        URLConnection deckURl;
        try {
            deckURl = new URL("https://deckofcardsapi.com/api/"
                    + "deck/new/shuffle/?deck_count=6").openConnection();

            Gson gson = dataWriter.create();

            Scanner deckHandler = new Scanner(deckURl.getInputStream());
            String data = deckHandler.useDelimiter("\\A").next();

            deck = gson.fromJson(data, Deck.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static InputStream getNewCard() {


        URLConnection deckURl;
        try {
            deckURl = new URL("https://deckofcardsapi.com/api/deck/" + deck.deck_id
                    + "/draw/?count=1").openConnection();

            return deckURl.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Card drawCards(){

        Card newCard;

        Gson gson = dataWriter.create();

        Scanner deckHandler = new Scanner(getNewCard());
        String data = deckHandler.useDelimiter("\\A").next();

        newCard = gson.fromJson(data, Card.class);

        return newCard;
    }

    static void addCardToPlayerHand(Card card, int playerID){

        switch (playerID){
            case 1 -> userHand.add(card);
            case 2 -> dealerHand.add(card);
        }

    }

}

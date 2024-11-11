package org.example.games;

import com.google.gson.*;
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

        JsonElement rootnode = JsonParser.parseString(data);

        JsonObject deckDetails = rootnode.getAsJsonObject();
        JsonArray cardDetails = deckDetails.getAsJsonArray("cards");

        String cardValues = String.valueOf(cardDetails);
        cardValues = removeBrackets(cardValues);

        newCard = gson.fromJson(cardValues, Card.class);

        return newCard;
    }


    private static String removeBrackets(String string){
        StringBuilder returningString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {

            char letter = string.charAt(i);

            if(letter == '[' || letter == ']'){
                continue;
            }

            returningString.append(letter);
        }
        return String.valueOf(returningString);}
}

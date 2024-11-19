package org.example.games;

import com.google.gson.*;
import org.example.Card;
import org.example.Deck;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Game extends JPanel {

    public static Deck deck;
    public static GsonBuilder dataWriter = new GsonBuilder();
    public static int shuffleCount = 0;

    public Game(){}

    /**
     * Creates a new deck using  the deckOfCardsAPI
     */
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

    /**
     * Gets a card from the deck at random using the deckOfCardsAPI
     * @return card's data
     */
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

    /**
     * after receiving card data, parses the data into the respective properties of
     * the card.
     *  <p> Calls {@link Game#getNewCard()} {@link Game#removeBrackets(String)}
     * @return newCard
     */
    static Card drawCards(){

        Card newCard;

        Gson gson = dataWriter.create();

        Scanner deckHandler = new Scanner(getNewCard());
        String data = deckHandler.useDelimiter("\\A").next();

        JsonElement rootNode = JsonParser.parseString(data);

        JsonObject deckDetails = rootNode.getAsJsonObject();
        JsonArray cardDetails = deckDetails.getAsJsonArray("cards");

        String cardValues = String.valueOf(cardDetails);
        cardValues = removeBrackets(cardValues);

        newCard = gson.fromJson(cardValues, Card.class);

        return newCard;
    }

    /**
     * removes brackets from data to allow it to be parsed directly to a subject
     * @param string to be cleaned
     * @return cleaned data
     */
    private static String removeBrackets(String string){
        StringBuilder returningString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {

            char letter = string.charAt(i);

            if(letter == '[' || letter == ']'){
                continue;
            }

            returningString.append(letter);
        }
        return String.valueOf(returningString);
    }

    /**
     * reshuffles the deck allowing for extended play
     */
    public static void shuffleDeck(){
        URLConnection deckURl;
        try {
            deckURl = new URL("https://deckofcardsapi.com/api/deck/" + deck.deck_id
                    + "/shuffle/").openConnection();

            Gson gson = dataWriter.create();

            Scanner deckHandler = new Scanner(deckURl.getInputStream());
            String data = deckHandler.useDelimiter("\\A").next();

            deck = gson.fromJson(data, Deck.class);

            shuffleCount++;
            System.out.println("Deck Shuffled, you are on shuffle " + shuffleCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

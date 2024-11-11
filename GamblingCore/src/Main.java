import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static double Money = 1000;

    static String deckID;
    static int remainingCards;
    static ArrayList <String> dealerHand = new ArrayList<>();
    static ArrayList <String> playersHandsBlackJack = new ArrayList<>();

    public static void main(String[] args) {


        Scanner deckHandler = new Scanner(createNewDeck());
        String data = deckHandler.useDelimiter("\\A").next();
        data = stringCleaner(data);

        System.out.println(data);

        dataReader(data,);

        System.out.println(deckID);
        System.out.println(remainingCards);

    }

    static InputStream createNewDeck() {


        URLConnection deckURl = null;
        try {
            deckURl = new URL("https://deckofcardsapi.com/api/"
                    + "deck/new/shuffle/?deck_count=6").openConnection();

            return deckURl.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static InputStream drawNewCard(){

        URLConnection deckURl = null;
        try {
            deckURl = new URL("https://deckofcardsapi.com/api/deck/" + deckID
                    + "/draw/?count=2").openConnection();

            return deckURl.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void dataReader(String data, String playerDrawing){
        Pattern pattern = Pattern.compile("(\\w+?):(\\w+?),");
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            switch (matcher.group(1)){
                case "deck_id": deckID = matcher.group(2);
                case "remaining": remainingCards = Integer.parseInt(matcher.group(2));
                case "code":
                    if (playerDrawing.equals("User")){

                    }
            }

        }

    }

    static String stringCleaner(String toBeCleaned){

        StringBuilder cleaned = new StringBuilder();

        for (int charLocation = 0; charLocation < toBeCleaned.length(); charLocation++) {

            char localChar = toBeCleaned.charAt(charLocation);

            if (localChar == '{' || localChar == ' ' || localChar == '"' || localChar == '}'
                || localChar == '[' || localChar == ']'){
                continue;
            }

            cleaned.append(localChar);


        }

        return String.valueOf(cleaned);
    }

}
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

    static double Money = 1000;

    static String deckID;
    static int remainingCards;

    public static void main(String[] args) {

        String newData = createNewDeck();

        dataReader(newData);

    }

    static String createNewDeck() {


        URLConnection deckURl = null;
        try {
            deckURl = new URL("https://deckofcardsapi.com/api/"
                    + "deck/new/shuffle/?deck_count=6").openConnection();

            return deckURl.getInputStream().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void dataReader(String data){

        int searchRow = 0;

        StringBuilder dataWriter = new StringBuilder();

        for (int charLocation = 0; charLocation < data.length(); charLocation++){

            char localChar = data.charAt(charLocation);

            switch(searchRow){
                case 1:
                    dataWriter.append(localChar);
                    deckID = String.valueOf(dataWriter);
                    break;
                case 2:
                    try {
                        if (Integer.parseInt(String.valueOf(localChar)) > -1) {
                            dataWriter.append(localChar);
                            try {
                                remainingCards = Integer.parseInt(String.valueOf(dataWriter));
                            } catch (NumberFormatException e) {
                                System.out.println("WE OVERESTIMATED OUR CODING ABILITIES");
                                throw new NumberFormatException();
                            }
                        }
                    }catch (NumberFormatException e){
                        break;
                    }
                    break;
                default:
                    break;
            }
            if(localChar == ','){
                dataWriter = new StringBuilder();
                searchRow++;
            }

        }

    }
}
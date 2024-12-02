package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Base64;

public class Card {


    public String code; // DISPLAYS AS JS (Jack of Spades)
    public String image; // LINK TO IMAGE
    public String value; // CAN HAVE VALUES OF 'JACK' OR 'KING'
    public String suit; // = SPADES, CLUBS, HEARTS, or DIAMONDS

    public Card() {}

    /**
     * gets the image of the card from the image string
     * @return image of card
     */
    public Image getCardImage() {
        if(image == null)
        {
            throw new NullPointerException();
        }try {
            URL url = new URI(image).toURL();
            return ImageIO.read(url);
        }catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public Image getCardBack(){
        try {
            URL url = new URI("https://deckofcardsapi.com/static/img/back.png").toURL();
            return ImageIO.read(url);
        }catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

}

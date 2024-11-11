package org.example;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

public class Card {


    public String code;
    public String image;
    public String value;
    public String suit;

    public Card() {}


    /**
     *
     * @param cardPNGPath the download path of the card
     */
    private void downloadCardPNG(String cardPNGPath) {

        try {
            URL cardPNG = new URL(cardPNGPath);
            URLConnection connection = cardPNG.openConnection();

            byte[] imageData = new byte[2048];
            File downloadFile = new File(code + "Card" + value + "Of" + suit + ".png");
            FileOutputStream outputStream = new FileOutputStream(downloadFile);
            int read;
            while ((read = connection.getInputStream().read(imageData)) != -1) {
                outputStream.write(imageData, 0, read);
            }
            outputStream.close();
            System.out.println(isBase64(downloadFile.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * detects if it is actually a picture
     * @param path the path to the image
     * @return True or False
     */
    static boolean isBase64(String path) {
        try {
            Base64.getDecoder().decode(path);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

}

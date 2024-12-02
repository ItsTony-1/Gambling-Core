package org.example;

import org.example.games.BlackJack;
import org.example.games.Game;
import org.example.games.TexasHoldEm;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {

    static Scanner console = new Scanner(System.in);
    static BlackJack blackJack = new BlackJack();
    static TexasHoldEm texasHoldEm = new TexasHoldEm();

    static JFrame frame = new JFrame();

    public static void main(String[] args) {

        createGUI();

        /*
        boolean gameSelected = false;
        String userInput;

        while (!gameSelected) {
            System.out.println("What game would you like to play? (Blackjack or Texas Hold Em");

            userInput = "Blackjack"; //console.nextLine(); //

            switch (userInput){
                case "Blackjack" -> {
                    gameSelected = true;
                    playBlackJack();
                }
                case "Texas Hold Em" -> {
                    gameSelected = true;
                    playTexasHolEm();
                }
                default -> System.out.println("Not a game. Please choose a game or double check spelling");
            }
        } */
    }

    private static void createGUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel contentPane = new JPanel();

        contentPane.setBorder(
                BorderFactory.createLineBorder(Color.BLACK, 5));
        contentPane.setLayout(new CardLayout());
        contentPane.setPreferredSize(new Dimension(1440,810));



        //region main menu
        JPanel mainScreen = new JPanel();
        JPanel mainScreenInner = new JPanel();

        mainScreen.setVisible(true);
        mainScreen.setLayout(new BoxLayout(mainScreen, BoxLayout.PAGE_AXIS));

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(mainScreenInner);
        horizontalBox.add(Box.createHorizontalGlue());
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(horizontalBox); // one inside the other
        verticalBox.add(Box.createVerticalGlue());

        mainScreen.add(verticalBox);
        mainScreen.setPreferredSize(new Dimension(1440,810));

        mainScreenInner.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel qAsker = new JLabel("Would you like to play Black Jack or Texas Hold 'Em");
        qAsker.setSize(1000,700);
        qAsker.setVisible(true);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 40;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainScreenInner.add(qAsker, constraints);

        JButton blackJackButton = new JButton("BlackJack");
        blackJackButton.setVisible(true);
        blackJackButton.setSize(600,300);
        blackJackButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.next(contentPane);
            //playBlackJack();
        });

        constraints.ipady = 40;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainScreenInner.add(blackJackButton, constraints);


        JButton texasHold = new JButton("Texas Hold 'Em");
        texasHold.setVisible(true);
        texasHold.setSize(600,300);
        texasHold.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.last(contentPane);
            playTexasHolEm();
        });

        constraints.ipady = 40;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainScreenInner.add(texasHold, constraints);



        mainScreenInner.revalidate();
        mainScreenInner.repaint();
        //endregion


        contentPane.add(mainScreen, "Main Menu");
        contentPane.add(blackJack, "Black Jack");
        contentPane.add(texasHoldEm, "Texas Hold 'Em");

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        frame.revalidate();
        frame.repaint();
    }

    private static void playTexasHolEm() {
        System.out.println("ERROR NOT AVAILABLE");
    }

    private static void playBlackJack() {

        boolean playing = true;

        while(playing){

            blackJack.startPlaying(true);
            blackJack.startPlaying(false);

            blackJack.getUserInput();

            System.out.println("Would you like to play again? [Y]/[N]");
            while(true) {
                String userInput = console.nextLine();

                if (userInput.equalsIgnoreCase("no") ||
                        userInput.equalsIgnoreCase("n")){
                    playing = false;
                    break;
                } else if (blackJack.money <= 4) {
                    System.out.println("DAMN YOU BROKE, GET AWAY FROM OUR TABLE");
                    playing = false;
                    break;
                } else if (userInput.equalsIgnoreCase("yes") ||
                            userInput.equalsIgnoreCase("y")) {
                    break;
                }

                System.out.println("Invalid input");
            }

            if (Double.parseDouble(Game.deck.remaining) < Math.round(Double.parseDouble(Game.deck.remaining) * 0.60)){
                Game.shuffleDeck();
            }
        }
        if (blackJack.money > 2500){
            System.out.println("You left with $" + blackJack.money + ". That is a gain of $" +
                    (blackJack.money - 2500));
        } else if (blackJack.money < 2500) {
            System.out.println("You left with $" + blackJack.money + ". That is a loss of $" +
                    (2500 - blackJack.money));
        }
    }
}
package org.example.games;

import org.example.Card;

import java.util.ArrayList;

public class Hand extends ArrayList <Card> {


    String handName;

    //region BLACKJACK
    int total;
    boolean hasAce;
    boolean hasDuplicate;
    boolean handSplit;
    //endregion

    //region POKER
    boolean hasFolded;
    int highCard;
    boolean hasOnePair; // 2 of a kind
    int onePairValue;
    boolean hasTwoPair; // 2 of a kind twice
    int twoPairValueOne;
    int twoPairValueTwo;
    boolean hasThreeOfAKind; // three of a kind
    int threeOfAKindValue;
    boolean hasStraight; // 5 cards in a row
    int straightValue; //Highest Card
    boolean hasFullHouse; // a pair and a 3 of a kind
    int fullHouseTOKValue;
    int fullHousePairValue;
    boolean hasFourOfAKind; // four of a kind
    int fourOfAKindValue;
    boolean hasStraightFlush; // Same Suit 5 in a row
    int straightFlushValue;
    String straightFlushSuit;
    boolean hasRoyalFlush; // same suit 5 straight of face kards + 10 and Ace

    /*
              Winning pyramid
                Royal Flush
        Straight Flush, Highest Peak
            Four of a Kind, Value
        Full house, By three pair, by 2 pair
            Straight, highest peak
            Three of a kind, Value
        Two Pair, pair 1 value & pair 2 value
               One Pair, Value
     */
    //endregion

    public Hand(boolean handSplit, String handName){
        this.handName = handName;

        this.handSplit = handSplit;
        this.total = 0;
        this.hasAce = false;
        this.hasDuplicate = false;


        highCard = 0;
        hasFolded = false;
        hasOnePair = false;
        hasTwoPair = false;
        hasThreeOfAKind = false;
        hasStraight = false;
        hasFullHouse = false;
        hasFourOfAKind = false;
        hasStraightFlush = false;
        hasRoyalFlush = false;

        onePairValue = 0;
        twoPairValueOne = 0;
        twoPairValueTwo = 0;
        threeOfAKindValue = 0;
        straightValue = 0;
        fullHouseTOKValue = 0;
        fullHousePairValue = 0;
        fourOfAKindValue = 0;
        straightFlushValue = 0;
        straightFlushSuit = "ERROR NO SUIT";
    }
}

package org.example.games;

import org.example.Card;

import java.util.ArrayList;

public class Hand extends ArrayList <Card> {

    int total;
    boolean hasAce;
    boolean handSplit;
    String handName;

    public Hand(boolean handSplit, String handName){
        this.handSplit = handSplit;
        this.total = 0;
        this.hasAce = false;
        this.handName = handName;
    }
}

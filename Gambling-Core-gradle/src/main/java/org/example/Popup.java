package org.example;

import javax.swing.*;
import java.awt.*;

public class Popup extends JFrame {

    JLabel winOrLose = new JLabel();

    JLabel winCondition = new JLabel();

    JButton confirm = new JButton("confirm");

    public Popup(boolean bustOrBlackJack){

        JPanel holder = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        this.setPreferredSize(new Dimension(500, 500));
        holder.setPreferredSize(new Dimension(500, 500));

        // Bust is false
        if (bustOrBlackJack){
            winOrLose.setText("You Win");
        }else {
            winOrLose.setText("You Lose");
        }

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 20;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        holder.add(winOrLose, constraints);

        constraints.gridy = 1;
        holder.add(winCondition, constraints);

        constraints.gridy = 2;
        holder.add(confirm, constraints);

    }

    public void setWinCondition(String winConditionText){
        winCondition.setText(winConditionText);
    }
}

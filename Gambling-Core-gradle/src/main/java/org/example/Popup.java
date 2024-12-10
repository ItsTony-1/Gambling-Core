package org.example;

import javax.swing.*;
import java.awt.*;

public class Popup extends JFrame {

    JLabel winOrLoseText = new JLabel();

    JLabel winCondition = new JLabel();

    public static JButton confirm = new JButton("confirm");

    public Popup(boolean winOrLose, boolean isPush, String winConditionText){

        JPanel holder = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Bust is false
        if (winOrLose){
            this.winOrLoseText.setText("You Win");
        } else if (isPush) {
            this.winOrLoseText.setText("Push");
        } else {
            this.winOrLoseText.setText("You Lose");
        }

        winCondition.setText(winConditionText);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 20;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        holder.add(this.winOrLoseText, constraints);

        constraints.gridy = 1;
        holder.add(winCondition, constraints);

        constraints.gridy = 2;
        holder.add(confirm, constraints);

        this.setVisible(true);
        holder.setVisible(true);
        winOrLoseText.setVisible(true);
        winCondition.setVisible(true);

        this.add(holder);

        this.setSize(new Dimension(800, 800));

        this.setPreferredSize(new Dimension(800, 800));
        holder.setPreferredSize(new Dimension(800, 800));
    }
}

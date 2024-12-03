package org.example;

import javax.swing.*;
import java.awt.*;

public class Popup extends JFrame {

    JLabel winOrLoseText = new JLabel();

    JLabel winCondition = new JLabel();

    public static JButton confirm = new JButton("confirm");

    public Popup(boolean winOrLose){

        JPanel holder = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        this.setPreferredSize(new Dimension(500, 500));
        holder.setPreferredSize(new Dimension(500, 500));

        // Bust is false
        if (winOrLose){
            this.winOrLoseText.setText("You Win");
        }else {
            this.winOrLoseText.setText("You Lose");
        }

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
    }

    public void setWinCondition(String winConditionText){
        winCondition.setText(winConditionText);
        this.revalidate();
        this.repaint();
    }
}

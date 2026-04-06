/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paintbrush;

import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author samar
 */
public class PaintBrush {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame myFrame = new JFrame();
        myFrame.setTitle("Draw Lines");
        myFrame.setSize(700, 500);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel myPanel = new MyPanel();
        myPanel.setBackground(Color.WHITE);
        myFrame.setContentPane(myPanel);
        myFrame.setVisible(true);
    }
    
}

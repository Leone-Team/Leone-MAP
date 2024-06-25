/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.gui;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        // Imposta il titolo del JFrame
        super("Frame con ImageBackGroundPanel");

        String backgroundPath = "/img/computerLeone.png"; 

        // Crea un'istanza di ImageBackGroundPanel
        ImageBackGroundPanel backgroundPanel = new ImageBackGroundPanel(backgroundPath);

        // Aggiungi il pannello al JFrame
        this.add(backgroundPanel);

        // Configura il JFrame
        this.setSize(860, 480);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Crea e visualizza il JFrame in Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}


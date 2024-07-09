package di.uniba.leone.gui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author feder
 */
public class Window extends JFrame {

    private final Color TEXTCOLOR = Color.yellow;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final String IMAGEPATH = "./src/main/resources/img/solopcleone.png";

    private BackgroundPanel mainPanel;
    private JTextPane display;
    private JScrollPane scrollDisplay;
    private JPanel bottomPanel;
    private JTextField inputField;
    private JButton enterBtn;
    private JLabel label1;
    private JPanel centerPanel;

    public Window(String title) {
        super(title);
        init(WIDTH, HEIGHT);
    }

    class BackgroundPanel extends JPanel {

        private final Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            if (backgroundImage != null) {
                return new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
            } else {
                return super.getPreferredSize();
            }
        }

    }

    private void init(int width, int height) {

        this.setSize(width, height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon backgroundImageIcon = new ImageIcon(IMAGEPATH);
        Image backgroundImage = backgroundImageIcon.getImage();

        mainPanel = new BackgroundPanel(backgroundImage);
        mainPanel.setLayout(new GridBagLayout());

        display = new JTextPane();
        scrollDisplay = new JScrollPane(display);

        bottomPanel = new JPanel(new GridBagLayout());
        inputField = new JTextField("");
        enterBtn = new JButton(">");
        label1 = new JLabel(">");

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.setBorder(new EmptyBorder(80, 80, 80, 80));

        //constraint per inputField
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 0, 10);

        inputField.setBorder(null);
        inputField.setOpaque(false);
        bottomPanel.add(inputField, gbc);
        //constraint per enterBtn
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        Dimension enterBtnDim = new Dimension(50, 30);
        enterBtn.setPreferredSize(enterBtnDim);
        enterBtn.setMaximumSize(enterBtnDim);
        enterBtn.setMinimumSize(enterBtnDim);

        enterBtn.setOpaque(false);
        bottomPanel.add(enterBtn, gbc);
        //constraint per label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        Dimension label1Dim = new Dimension(50, 30);
        label1Dim.setSize(label1Dim);

        label1.setOpaque(false);
        bottomPanel.add(label1, gbc);

        bottomPanel.setOpaque(false);

        display.setFocusable(false);
        display.setOpaque(false);
        scrollDisplay.setOpaque(false);
        scrollDisplay.getViewport().setOpaque(false);
        scrollDisplay.setBorder(null);
        scrollDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        centerPanel.setOpaque(false);
        centerPanel.add(scrollDisplay, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(30, 30, 10, 30);
        mainPanel.add(centerPanel, gbc);

        this.add(mainPanel, BorderLayout.CENTER);

        this.pack();
        
        display.setForeground(TEXTCOLOR);
        inputField.setForeground(TEXTCOLOR);
        label1.setForeground(TEXTCOLOR);
        
        this.setResizable(false);
        this.setVisible(true);

//        MsgManager mrMsg = new MsgManager(display, inputField);
//        Action sendMessageAction = new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mrMsg.enterMsg();
//            }
//        };
//
//        enterBtn.addActionListener(sendMessageAction);
//        inputField.addActionListener(sendMessageAction);
    }

    public JTextPane getDisplay() {
        return display;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public JButton getEnterBtn() {
        return enterBtn;
    }
    
    


}

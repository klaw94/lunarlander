package moonlander;

import javax.swing.*;
import java.awt.*;

public class GuiMoonLander extends JPanel {
    JFrame frame;
    MyPanel fieldPanel;
    JPanel textPanel;
    JLabel textField;

    int unitSize = 5;



    public GuiMoonLander(int size){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("MoonLander");
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        fieldPanel = new MyPanel(size, unitSize);
        fieldPanel.setBackground(Color.GRAY);
        fieldPanel.setVisible(true);

        textPanel = new JPanel();
        textPanel.setVisible(true);
        textPanel.setBackground(Color.DARK_GRAY);

        textField = new JLabel();
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setForeground(Color.white);
        textField.setFont(new Font("Mv Boli", Font.BOLD, 15));
        textField.setText("Speed : " + 0 + ";   Fuel : " + 0 +  ";   Time : " + 0);

        frame.add(textPanel, BorderLayout.NORTH);
        textPanel.add(textField);
        frame.add(fieldPanel);
        frame.setSize(new Dimension(size + 35, size + 35));
        fieldPanel.setText(textField);
        frame.revalidate();

    }



    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GuiMoonLander(600);
            }
        });

    }
}
package moonlander;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MyPanel extends JPanel implements ActionListener {
    int height;
    int size;
    int unitSize;
    int speed;
    int horizontalSpeed;
    JLabel text;
    boolean gameOver = false;
    int[][]mountainCoordinates;
    int altitude;
    int time = 0;
    Rocket rocket;
    Timer timer;
    Timer horizontalTimer;
    int originalDelay = 100;
    int horizontalDelay = 100;
    int realDelay = 100;
    boolean didIWin = false;


    public MyPanel(int size, int unitSize) {
        this.height = size - 25;
        this.size = size;
        this.unitSize = unitSize;
        setBorder(BorderFactory.createLineBorder(Color.black));
        mountainCoordinates = new int[650/unitSize][2];
        populateMountainCoordinates();
        rocket = new Rocket(unitSize);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new MyKeyAdapter());
        this.addKeyListener(new ReleasedKeyAdapter());
        this.setVisible(true);

        startGame();
    }

    private void startGame() {
        timer = new Timer(realDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rocket.moveDown();
                if(originalDelay < realDelay){
                    resetDelay();
                    updateLabel();
                }
                checkLanded();
                if(!didIWin && !gameOver){
                    checkCollision();

                }
                repaint();
            }
        });
        timer.setRepeats(true);
        timer.setInitialDelay(100);
        timer.start();

        horizontalTimer = new Timer(horizontalDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rocket.moveHorizontal();

                checkLanded();
                if(!didIWin && !gameOver){
                    checkCollision();

                }
                repaint();
            }
        });
        horizontalTimer.setRepeats(true);
        horizontalTimer.setInitialDelay(100);
        horizontalTimer.start();
    }

    private void checkLanded() {
        if((rocket.yCoordinates[rocket.yCoordinates.length - 1] == 520 && rocket.xCoordinates[0] > 220 && rocket.xCoordinates[0] < 430 - 15) && speed <= 20 && horizontalSpeed <= 20){
            timer.stop();
            horizontalTimer.stop();
            didIWin = true;
            repaint();
            text.setText("You Landed!");
            rocket.removeTail();

        } else if ((rocket.yCoordinates[rocket.yCoordinates.length - 1] == 520 && rocket.xCoordinates[0] > 220 && rocket.xCoordinates[0] < 430 - 15) && speed > 20 || horizontalSpeed > 20){
            timer.stop();
            horizontalTimer.stop();
            text.setText("You went too fast! You Lost");
            gameOver = true;
            repaint();
            rocket.removeTail();
        }
    }

    private void resetDelay() {
        if(realDelay >= 100){
            realDelay -= 20;
            timer.setDelay(realDelay);
        } else {
            realDelay -=1;
            timer.setDelay(realDelay);
        }

    }

    private void checkCollision() {
        for (int i = 0; i < mountainCoordinates.length; i++){
            for(int x = 0; x < rocket.yCoordinates.length; x++){
                for(int y = 0; y < rocket.xCoordinates.length; y++){
                    if ( mountainCoordinates[i][0] == rocket.xCoordinates[y] && mountainCoordinates[i][1] == rocket.yCoordinates[x] ){
                        timer.stop();
                        horizontalTimer.stop();
                        text.setText("You Lost!");
                        gameOver = true;
                        rocket.removeTail();
                        repaint();
                    }
                }
            }

        }
    }

    public void setText(JLabel text){
        this.text = text;
    }

    private void populateMountainCoordinates() {
        for (int i = 0; i < mountainCoordinates.length; i++){
            if( size/2 + i * unitSize < 520) {
                mountainCoordinates[i][0] = i * unitSize;
                mountainCoordinates[i][1] = size / 2 + i * unitSize;
            } else {
                mountainCoordinates[i][0] = i * unitSize;
                mountainCoordinates[i][1] = 520;
            }
        }
        for (int i = mountainCoordinates.length - 1; i >0; i--) {
            int x = mountainCoordinates.length - 1 - i;
            if (size / 2 + x * unitSize < 520) {
                mountainCoordinates[i][0] = i * unitSize;
                mountainCoordinates[i][1] = size / 2 + x * unitSize;
            } else {
                return;
            }
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw
        draw(g);
    }
    public void draw(Graphics g){
        g.setColor(Color.yellow);
        for (int i = 0; i < mountainCoordinates.length; i++){
            g.fillRect(mountainCoordinates[i][0],mountainCoordinates[i][1], 5, 5);
        }

        for(int i = 0; i < rocket.length; i++){
            for(int x = 0; x < rocket.width; x++) {
                if((i == 0 && x == 0) || (i == 0 && x == rocket.width -1 )){
                    continue;
                }else if(i == rocket.length - 1 && (x == 1 || x == 3)) {
                   continue;
                } else{
                    if (gameOver) {
                        g.setColor(Color.red);
                    } else if(didIWin){
                        g.setColor(Color.blue);
                    } else {

                        g.setColor(Color.green);
                    }

                    g.fillRect(rocket.xCoordinates[x], rocket.yCoordinates[i], 5, 5);
                }

            }
        }
        if (rocket.doIHaveTail()){
            for (int i = 0; i < rocket.tailCoordinates.length; i++){
                g.setColor(Color.red);
                g.fillRect(rocket.tailCoordinates[i][0],rocket.tailCoordinates[i][1], 5, 5);
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if(!didIWin && !gameOver){
                        if(rocket.getFuel()>0){
                            //rocket.changeDirection('R');
                            if (rocket.getDirection() == 'R' && horizontalDelay > 100) {
                                horizontalDelay -= 10;
                            } else if (rocket.getDirection() != 'R' && horizontalDelay < 500){
                                horizontalDelay += 10;
                            }
                            horizontalTimer.setDelay(horizontalDelay);
                            rocket.paintTail('R');
                            rocket.consumeFuel();
                            checkChangeOfDirection();
                            updateLabel();
                        }
                    }

                    break;
                case KeyEvent.VK_LEFT:
                    if(!didIWin && !gameOver) {
                        if(rocket.getFuel()>0) {
                            if (rocket.getDirection() == 'L' && horizontalDelay > 100) {
                                horizontalDelay -= 10;
                            } else if (rocket.getDirection() != 'L' && horizontalDelay < 500){
                                horizontalDelay += 10;
                            }
                           // rocket.changeDirection('L');
                            horizontalTimer.setDelay(horizontalDelay);
                            rocket.paintTail('L');
                            rocket.consumeFuel();
                            checkChangeOfDirection();
                            updateLabel();
                        }
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(!didIWin && !gameOver) {
                        if(rocket.getFuel()>0) {
                            if (realDelay < 500) {
                                realDelay += 10;
                            }
                            timer.setDelay(realDelay);
                            rocket.paintTail('U');
                            rocket.consumeFuel();
                            updateLabel();
                        }
                    }
                    break;
                    case KeyEvent.VK_SPACE:
                        if (gameOver || didIWin ) {
                            rocket = new Rocket(unitSize);
                            startGame();
                            rocket.resetFuel();
                            text.setText("Speed : " + 0 + ";   Fuel : " + rocket.getFuel() +  ";   Time : " + 0);
                            gameOver = false;
                            didIWin = false;
                        }
                        break;

            }
        }
    }

    private void checkChangeOfDirection() {
        if(horizontalDelay >= 490){
            if(rocket.getDirection() == 'R'){
                rocket.changeDirection('L');
                horizontalDelay -= 10;
            } else {
                rocket.changeDirection('R');
                horizontalDelay -= 10;
            }

            horizontalTimer.setDelay(horizontalDelay);
        }
    }

    private void updateLabel() {
        speed = 50 - realDelay/10;
        horizontalSpeed = 50 - horizontalDelay/10;
        text.setText( "Speed : " + speed + ";   Horizontal Speed : " + horizontalSpeed + ";   Fuel : " + rocket.getFuel() +  ";   Time : " + 0);
    }


    public class ReleasedKeyAdapter extends KeyAdapter {
            @Override

            public void keyReleased(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_UP:
                        rocket.removeTail();
                break;

                }
    }
}


    }




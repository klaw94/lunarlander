package moonlander;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Rocket {
    int unitSize;
    int length = 8;
    int width = 5;
    int[]headPosition = new int[2];
    int[]xCoordinates;
    int[]yCoordinates;
    char direction = 'R';
    int[][] tailCoordinates = new int [4][2];
    boolean hasTail = false;
    int fuel = 700;

    public Rocket (int unitSize){
        this.unitSize = unitSize;
        xCoordinates =  new int[width];
        yCoordinates = new int[length];
        populateCoordinates();
        headPosition[0] = 200;
        headPosition[1]= 200;
    }

    private void populateCoordinates() {
        for (int i = 0; i < length; i++){
            yCoordinates[i] = 200 + i * 5;

        }

        for (int i = 0; i < width; i++){
            xCoordinates[i] = 200 + i * 5;

        }




    }

    public void move() {
        if(direction == 'R'){
            headPosition[0]+= 5;
            headPosition[1]+= 5;
        } else if(direction == 'L'){
            headPosition[0]+= 5;
            headPosition[1]-= 5;
        }

        for (int i = 1; i < length; i++){
            yCoordinates[i] = headPosition[0] + i * 5;
        }

        for (int x =0; x < width; x++){
            xCoordinates[x] = headPosition[1] + x * 5;
        }

        yCoordinates[0] = headPosition[0];
        xCoordinates[0] = headPosition[1];
    }


    public void changeDirection(char direction) {
        this.direction = direction;
    }

    public void paintTail(char direction) {
        hasTail = true;
        if(direction == 'R'){
            for(int i = 0; i < tailCoordinates.length; i ++){
                tailCoordinates[i][0]= xCoordinates[(width - 1)/2] - i * 5;
                tailCoordinates[i][1]= yCoordinates[length - 1] + 5;
            }

        } else if(direction == 'L'){
            for(int i = 0; i < tailCoordinates.length; i ++){
                tailCoordinates[i][0]= xCoordinates[(width - 1)/2] + i * 5;
                tailCoordinates[i][1]= yCoordinates[length - 1] + 5;
            }
        }else {
            for(int i = 0; i < tailCoordinates.length; i ++){
                tailCoordinates[i][0]= xCoordinates[(width - 1)/2];
                tailCoordinates[i][1]= (yCoordinates[length - 1] + i * 5) + 5;
            }
        }
    }

    public boolean doIHaveTail(){
        return hasTail;
    }


    public void removeTail() {
        hasTail = false;
    }

    public void consumeFuel() {
        if(fuel > 0){
            fuel -= 10;
        }
    }

    public int getFuel() {
        return fuel;
    }

    public void resetFuel(){
        fuel = 700;
    }
}

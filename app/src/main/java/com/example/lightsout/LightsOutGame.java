//MODEL
package com.example.lightsout;

import java.util.Random;
//This class generates pseudorandom numbers

public class LightsOutGame {
    //declaring new class LightsOutGame

    public static final int GRID_SIZE = 3;
    //Making a new constant for the grid size, it will be used quite a bit


    private final boolean[][] mLightsGrid;
    /*2d array, displays like this (but not quite yet)
        {{T, F, F},
         {T, F, T},
         {F, F, T}}
     or it can be on one line, this just gives a better idea*/

    public LightsOutGame() {
        //This is used in MainActivity to create an instance of this class over there

        mLightsGrid = new boolean[GRID_SIZE][GRID_SIZE];
        //this sets mLightsGrid 2d array size to 3x3
    }

    public void newGame() {
        //This is the newGame method, it randomly turns on all the lights

        Random randomNumGenerator = new Random();
        //Here we are creating a new instance of the random class called randomNumGenerator

        for (int row = 0; row < GRID_SIZE; row++) {
            //here we are iterating though every row and doing the things in the curly braces

            for (int col = 0; col < GRID_SIZE; col++) {
                //here we are iterating though every column and doing the things in the curly braces, long story short we are iterating though every grid.

                mLightsGrid[row][col] = randomNumGenerator.nextBoolean();
                //This is just assigning a random True or False to the current grid thanks to nextBoolean.
            }
        }
    }

    public boolean isLightOn(int row, int col) {
        //This just returns weather or not the light in the selected grid is on

        return mLightsGrid[row][col];
        //You just supply it with the row and column and it will tell you if its on or off.
    }

    public void selectLight(int row, int col) {
        //This is what inverts the lights when you tap them, it takes the coordinates and passes them along though the method

        mLightsGrid[row][col] = !mLightsGrid[row][col];
        //Here it immediately inverts the grid you touched

        if (row > 0) {
            //Here its checking if your row is greater than zero

            mLightsGrid[row - 1][col] = !mLightsGrid[row - 1][col];
            //Here it is moving down one row and inverting that light
        }
        if (row < GRID_SIZE - 1) {
            //Here it is checking if your row is less than the grid size -1

            mLightsGrid[row + 1][col] = !mLightsGrid[row + 1][col];
            //Here it is moving up one row and inverting that light
        }
        if (col > 0) {
            //Here it is checking if the column is greater than 0

            mLightsGrid[row][col - 1] = !mLightsGrid[row][col - 1];
            //Here it is moving down one column and inverting that light
        }
        if (col < GRID_SIZE - 1) {
            //Here it is checking if the column is less than the grid size - 1

            mLightsGrid[row][col + 1] = !mLightsGrid[row][col + 1];
            //here it moving up one column and inverting that light
        }
    }

    public boolean isGameOver() {
        //This method goes over each light and checks if its on

        for (int row = 0; row < GRID_SIZE; row++) {
            //here we are iterating though every row and doing the things in the curly braces

            for (int col = 0; col < GRID_SIZE; col++) {
                //here we are iterating though every column and doing the things in the curly braces, long story short we are iterating though every grid.

                if (mLightsGrid[row][col]) {
                    //here it is just looking at the light and if its on, it will return false.

                    return false;
                    //This is where it would return false if the light is on.
                }
            }
        }
        return true;
        //This is where it would return true if no lights were on
    }

    public String getState() {
        //This gets the current state of all the lights

        StringBuilder boardString = new StringBuilder();
        //Here we are creating a new instance of the StringBuilder class named boardString. This will hold the state of the lights.

        for (int row = 0; row < GRID_SIZE; row++) {
            //here we are iterating though every row and doing the things in the curly braces

            for (int col = 0; col < GRID_SIZE; col++) {
                //here we are iterating though every column and doing the things in the curly braces, long story short we are iterating though every grid.

                char value = mLightsGrid[row][col] ? 'T' : 'F';
                //basically it takes the current grid button: then checks if its true, if its true it will append a 'T' to the boardString, if not it will append a 'F'

                boardString.append(value);
                //Here it just appends the character to the end of boardString
            }
        }

        return boardString.toString();
        //This is just returning the string we just built, it will be used to recover after the device is rotated primarily
    }

    public void setState(String gameState) {
        //This is used in conjunction with getState to recover the state of the lights

        int index = 0;
        //new int named index, it will be used to index. More specifically, it will be used to index through the gameState.

        for (int row = 0; row < GRID_SIZE; row++) {
            //here we are iterating though every row and doing the things in the curly braces

            for (int col = 0; col < GRID_SIZE; col++) {
                //here we are iterating though every column and doing the things in the curly braces, long story short we are iterating though every grid.

                mLightsGrid[row][col] = gameState.charAt(index) == 'T';
                /* This took me so long to understand, but it's not assigning the characters to the grid buttons, its checking weather or not the current char is
                equivalent to 'T' and if it is the current grid button will be set to true and if not it will be set to false. I couldn't wrap my head around how
                it was assigning a char to a boolean */

                index++;
                //index +1
            }
        }
    }

    //This doesn't work exactly how I want it to, the button registers a press after the tiles are all turned off,
    // still gets you the win but not really what I wanted.
    public void cheat() {
        //This method is a little hidden cheat code, you long press the top left grid and all the tiles are *supposed to turn off

        for (int row = 0; row < GRID_SIZE; row++) {
            //here we are iterating though every row and doing the things in the curly braces

            for (int col = 0; col < GRID_SIZE; col++) {
                //here we are iterating though every column and doing the things in the curly braces, long story short we are iterating though every grid.

                mLightsGrid[row][col] = false;
                //turns the current grid off, since we are going through every grid they will all be turned off.
            }
        }
    }
}
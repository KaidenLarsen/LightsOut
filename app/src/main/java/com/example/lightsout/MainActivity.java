package com.example.lightsout;

import android.app.Activity;
//This class is used to create windows in which you put your UI elements in

import android.content.Intent;
//An intent is a description of things to be done. It is used a lot in activities

import android.os.Bundle;
//A bundle is a container (I don't know if that's the right term) that holds data in key-value pairs

import android.view.View;
//This class is the basic building block for UI components

import android.widget.Button;
//Buttons, you can click them and they do an action

import android.widget.GridLayout;
//This layout puts all of its children in a neat grid with cells

import android.widget.Toast;
//This class lets you make little pop up messages at the bottom of the screen, maybe even in other parts. I haven't experimented with it.

import androidx.activity.result.ActivityResult;
//This class lets you handle results from other activities, at least that's what I have gathered

import androidx.activity.result.ActivityResultCallback;
//This class allows you to retrieve the results from another activity

import androidx.activity.result.ActivityResultLauncher;
//This class allows you to launch activities with the ability to send results,

import androidx.activity.result.contract.ActivityResultContracts;
//This class allows you to define an activities input and output

import androidx.annotation.NonNull;
//This class allows you to say that a parameter or field can never be null

import androidx.appcompat.app.AppCompatActivity;
//This class lets you use newer platform features on older Android devices.

import androidx.core.content.ContextCompat;
//I'm not sure what this is, we only really use it in this for .getColor. the docs say "Helper for accessing features in Context."


public class MainActivity extends AppCompatActivity {
//Here we are making a class named MainActivity that extends AppCompatActivity, what that means is we are inheriting features from AppCompatActivity

    private static final String KEY_LIGHT_STATE = "lightState";
    //Here we are making a key to be used in conjunction with the value mLightOnColorId that we will use to maintain the color of the lights when the activity is restarted.
    private int mLightOnColorId;
    //This hold resource IDs for the colors (R.color.yellow) not the actual color
    private final String GAME_STATE = "gameState";
    //This is another key, used in conjunction with the getState() method from over in LightsOutGame.
    private LightsOutGame mGame;
    //all of our methods in LightsOutGame are static therefore we need to create an instance of the class here in MainActivity
    private GridLayout mLightGrid;
    //This a GridLayout which we will be assigning to the GridLayout that holds all of the light-buttons.
    private int mLightOnColor;
    //This holds the color of the light-buttons when they are on.
    private int mLightOffColor;
    //This holds the color of the light-buttons when they are off.


    @Override
    /*Still not clear on override, this is what I've gathered:
     @Override is like saying "I'm going to provide my own version of this method, and when it's called use my version instead of the parent class version.*/

    protected void onCreate(Bundle savedInstanceState) {
    //This method is only called when an activity is created, it takes a bundle parameter so that it can recover after rotating for instance.

        super.onCreate(savedInstanceState);
        //I don't really know whats going on here, we're just passing onCreate the savedInstanceState bundle. I guess it's just setup for later.

        setContentView(R.layout.activity_main);
        //This is just setting the current view to activity_main.xml


        if (savedInstanceState != null ) {
            //If savedInstanceState is not null do whats in the curly braces

            mLightOnColorId = savedInstanceState.getInt(KEY_LIGHT_STATE);
            //Here we are setting the mLightOnColorId to the one paired to the KEY_LIGHT_STATE key

        } else {
            mLightOnColorId = R.color.yellow;
            //Here if the savedInstanceState is null the default color id will be set to yellow
            //Without this if else, the colors wouldn't last for more than two rotations because the color ID was being set to yellow at the
            //top of onCreate every time
        }

            mLightGrid = findViewById(R.id.light_grid);
            //This is assigning the GridLayout mLightGrid to the GridLayout from our UI


        // Add the same click handler to all grid buttons
        for (int buttonIndex = 0; buttonIndex < mLightGrid.getChildCount(); buttonIndex++) {
            //This is a for loop that iterates through each cell in the mLightGrid getting the count with getChildCount().

            Button gridButton = (Button) mLightGrid.getChildAt(buttonIndex);
            //This making it so the button at the current index is gridButton, this will be used on the next line

            gridButton.setOnClickListener(this::onLightButtonClick);
            //This is assigning an onClickListener to gridButton, its basically saying that when you click the gridButton I want onLightButtonClick to be called
        }

        mLightGrid.getChildAt(0).setOnLongClickListener(new View.OnLongClickListener() {
            //This is getting the cell at top left of mLightGrid and setting a long click listener on it
            @Override
            //see the @Override comment

            public boolean onLongClick(View view) {
                //This is what is called when a long click is ran, honestly not sure why it returns a boolean. I googled it and I has to do with weather or not the long
                //click event was "consumed". I can see why that would be useful, changed my return to true.

                mGame.cheat();
                //This calls the cheat() method from LightsOutGame

                return true;
                //This returns true, signaling that the long click was consumed/handled appropriately
            }
        });


        mLightOnColor = ContextCompat.getColor(this, R.color.yellow);
        //This sets the light on color to the yellow from the color file.

        mLightOffColor = ContextCompat.getColor(this, R.color.black);
        //This sets the light off color to the color black from the color file

        mGame = new LightsOutGame();
        //this create an instance of the class LightsOutGame here in MainActivity

        if (savedInstanceState == null) {
            //if the savedInstanceState is null or empty/not used or more simply put if the app hasn't had to be restored then:

            startGame();
            //call startGame

        }
        else {
            String gameState = savedInstanceState.getString(GAME_STATE);
            //here we are getting the value to the key from savedInstanceState and assigning it to the string gameState. This string should just be a string of 'T's and 'F's

            mGame.setState(gameState);
            //Here we are using that string gameState and passing it into the method setState, setState will use the string gameState to restore the position of the tiles if need be

            mLightOnColor = ContextCompat.getColor(this, savedInstanceState.getInt(KEY_LIGHT_STATE));
            //Here we are recovering the color of the tiles from the savedInstanceState and assigning it to the LightOnColor.

            setButtonColors();
            //This sets the button colors, without this here the button color wont update visually.
        }

    }

    public void onHelpClick(View view) {
        //This is what is called when you click the help button

        Intent intent = new Intent(this, HelpActivity.class);
        //here the intent is set to start HelpActivity class

        startActivity(intent);
        //Here we call startActivity passing our intent which is set to start HelpActivity - this opens the HelpActivity activity
    }


    @Override
    //See the override comment

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //before the activity is stopped, onSavedInstanceState is called to save any important data for restoring its state

        super.onSaveInstanceState(outState);
        //Similar to what happened in onCreate, I guess its just prep

        outState.putString(GAME_STATE, mGame.getState());
        //Here we are just putting the key GAME_STATE in the Bundle outState along with its value the output from mGame.getState()

        outState.putInt(KEY_LIGHT_STATE ,mLightOnColorId);
        //Similar thing here, just a bit simpler. putting the key KEY_LIGHT_STATE in the Bundle outState along with the value mLightOnColorId
    }

    private void startGame() {
        //This runs when you click the start game button

        mGame.newGame();
        //This calls the newGame method over in LightsOutGame

        setButtonColors();
        //This sets the button colors. Without this the buttons would all be invisible.
    }

    private void onLightButtonClick(View view) {
        //This is called whenever you click a light-button

        // Find the button's row and col
        int buttonIndex = mLightGrid.indexOfChild(view);
        //This gets the index of the child using the indexOfChild method

        int row = buttonIndex / LightsOutGame.GRID_SIZE;
        //This finds the row by dividing the index by the grid size, I would have never thought of it but good on them

        int col = buttonIndex % LightsOutGame.GRID_SIZE;
        //This finds the column using the remainder operator, I'm not going to pretend like I understand it

        mGame.selectLight(row, col);
        //This calls the selectLight method from LightsOutGame on the current cell, this method is responsible for inverting the lights

        setButtonColors();
        //Again, without this the buttons would be invisible

        // Congratulate the user if the game is over
        if (mGame.isGameOver()) {
            //If isGameOver returns true you'll get a congrats toast


            Toast.makeText(this, R.string.congrats, Toast.LENGTH_SHORT).show();
            //Here's the toast code, it uses the congrats string, with a short length
        }
    }

    private void setButtonColors() {
        //This is what's called when you hit the change color button

        for (int buttonIndex = 0; buttonIndex < mLightGrid.getChildCount(); buttonIndex++) {
            //This iterates through each child in the light grid, it got the count with getChildCount

            Button gridButton = (Button) mLightGrid.getChildAt(buttonIndex);
            //as its iterating through each child it is making it gridButton

            // Find the button's row and col
            int row = buttonIndex / LightsOutGame.GRID_SIZE;
            //Here it fins the buttons row by dividing the index by the grid size once again

            int col = buttonIndex % LightsOutGame.GRID_SIZE;
            //same here, column found by using the remainder operator on the index and grid size

            if (mGame.isLightOn(row, col)) {
                //isLightOn literally just returns the state of the light true or false, and its iterating through all the lights

                gridButton.setBackgroundColor(mLightOnColor);
                //If its on it will set it to whatever the current light on color is

                gridButton.setContentDescription(Integer.toString(R.string.on));
                //If its on it will set the content description to the string on, note: I tried to use the string but it said it was an int so I converted it. IDK if it is correct.
            } else {

                gridButton.setBackgroundColor(mLightOffColor);
                //If the light is off it will set the light to its current off color

                gridButton.setContentDescription(Integer.toString(R.string.off));
                //if the light is off this will set its content description to match, see note two comments above.
            }
        }
    }

    public void onChangeColorClick(View view) {
        // Send the current color ID to ColorActivity

        Intent intent = new Intent(this, ColorActivity.class);
        //here we are making a new intent and having it start the ColorActivity class

        intent.putExtra(ColorActivity.EXTRA_COLOR, mLightOnColorId);
        //We are putting the current light button color in the intent extra so we can send it over to ColorActivity

        mColorResultLauncher.launch(intent);
        //This launches whatever class currently specified by the intent, right now it is ColorActivity. This was also set up with the intent for a result.

    }

    //registerForActivityResult() returns an ActivityResultLauncher object. we are making one here called mColorResultLauncher.
    ActivityResultLauncher<Intent> mColorResultLauncher = registerForActivityResult(
            //Here we are calling registerForActivityResult which returns a ActivityResultLauncher so we are making one right in front of it called mColorResultLauncher

            new ActivityResultContracts.StartActivityForResult(),
            //This is the contract for registerForActivityResult, as far as I know this one doesn't do anything crazy

            new ActivityResultCallback<ActivityResult>() {
                //This is the callback, it is executed when the started activity sends back a result

                @Override
                //see the override comment

                public void onActivityResult(ActivityResult result) {
                    //this is ran whenever the activity sends back a result, the ActivityResult is just called result here

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //here we are checking if the results are being sent or if there are no results, if there were no results the code would probably be RESULT_CANCELLED

                        Intent data = result.getData();
                        //here we are converting the result to an Intent called data

                        if (data != null) {
                            // here we are checking if the Intent is null or empty, if its not continue

                            // Create the "on" button color from the chosen color ID from ColorActivity
                            mLightOnColorId = data.getIntExtra(ColorActivity.EXTRA_COLOR, R.color.yellow);
                            //here we are setting the mLightOnColorId to the EXTRA_COLOR's value, if it doesn't have one it will default to the value of yellow in colors.

                            mLightOnColor = ContextCompat.getColor(MainActivity.this, mLightOnColorId);
                            //Here we are setting the color of the lights when they are on to the LightOnColorId

                            setButtonColors();
                            //This sets the grid buttons colors, without this they would be invisible.
                        }
                    }
                }
            });

    public void onNewGameClick(View view)
            //when you click new game this is called

    {
        startGame();
        //This calls start game, start game just randomly turns on all the lights
    }
}
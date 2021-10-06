/*-----------------------------------------
SONGS USED
-------------------------------------------
https://www.youtube.com/watch?v=otD-XbAhMbU
https://www.youtube.com/watch?v=_8Em605BQz4
https://www.youtube.com/watch?v=Hn4sfC2PbhI
https://www.youtube.com/watch?v=9rujCfYXhQc
-----------------------------------------*/

package files;

import ddf.minim.*;
import ddf.minim.analysis.*;
import lib.processing.*;

import java.util.ArrayList;
import java.text.DecimalFormat;

public class Game extends Visual
{  

    //Was not working as intended with class arrays, so chose to use arraylists instead
    ArrayList <Float> fallingBeatsListX = new ArrayList<Float>();
    ArrayList <Float> fallingBeatsListY = new ArrayList<Float>();

    DecimalFormat df = new DecimalFormat("0.00");

    //Use another minim lib to detect beats - NOT to play music through speakers
    Minim minimAlt;
    AudioPlayer audioPlayerAlt;
    //FFT fft;

    boolean difficultySet = false;

    float noteFallSpeed = 5.6f;

    //Index variable
    int i = 0;

    float selectedX = 0;
    float selectedY = -400;

    //BeatDetect heart = new BeatDetect();
    //BeatDetect to detect the beat of song
    BeatDetect beatDetector;

    //Var for the game timer
    int timer = 0;
    int timer2 = 0;

    //Var to count player score
    float score = 0;

    //Var to count player score multiplier
    float scoreMultiplier = 1;
    //Var for streak and accuracy counters
    int noteStreak = 0;
    int missedNotes = 0;
    int hitNotes = 0;

    //Var for the hue of text etc.
    float hue = 0;
    float sat = 70;

    //Values to fill arrow controls
    int leftControlFill = 100;
    int downControlFill = 100;
    int upControlFill = 100;
    int rightControlFill = 100;

    //Vars to check if control is being pressed
    boolean leftPressed = false;
    boolean downPressed = false;
    boolean upPressed = false;
    boolean rightPressed = false;

    //radius
    float eRadius;

    public void settings()
    {
        size(960, 600);
        
        //Makes processing fullscreen
        fullScreen();
    }

    public void setup()
    {
        startMinim();
        background(0);
        
        minimAlt = new Minim(this);
        audioPlayerAlt = minimAlt.loadFile("Alibi.mp3");
        loadAudio("Alibi.mp3");

        //Beat detector
        beatDetector = new BeatDetect();
        beatDetector.setSensitivity(390);

        
        colorMode(HSB, 360);

        ellipseMode(RADIUS);
        eRadius = 20;

    }

    public void keyPressed()
    {
        //Stops game if space is pressed
        if (getAudioPlayer().isPlaying())
        {

            if (key == ' ')
            {
                background(0);
                getAudioPlayer().pause();
                audioPlayerAlt.pause();
                score = 0;
                difficultySet = false;

            }

            if (key == CODED)
            {
                if (keyCode == LEFT)
                {
                    leftControlFill = 200;
                    timer = frameCount;
                    leftPressed = true;
                    
                }

                if (keyCode == DOWN)
                {
                    downControlFill = 200;
                    timer = frameCount;
                    downPressed = true;
                }

                if (keyCode == UP)
                {
                    upControlFill = 200;
                    timer = frameCount;
                    upPressed = true;
                }

                if (keyCode == RIGHT)
                {
                    rightControlFill = 200;
                    timer = frameCount;
                    rightPressed = true;
                }

            }

        }

    }

    public void draw()
    {

        try
        {
            calculateFFT(); 
        }
        catch(VisualException e)
        {
            e.printStackTrace();
        }

        calculateFrequencyBands(); 
        calculateAverageAmplitude();    

        int leftArrowButtonX = width/2-225;//-225
        int downArrowButtonX = width/2-75;//-75
        int upArrowButtonX = width/2+75;//+75
        int rightArrowButtonX = width/2+225;//+225

        boolean noteHit = false;

        runGame();
        detectBeats();

        mainMenu();

    if (getAudioPlayer().isPlaying() || audioPlayerAlt.isPlaying()) {

        fill(round(hue), sat, sat+100);
        stroke(round(hue), sat, sat+100);

        for(int i = 0; i <  fallingBeatsListX.size(); i++) {
            textSize(250);
            if(fallingBeatsListX.get(i) == leftArrowButtonX) {
                text("⇐", leftArrowButtonX, fallingBeatsListY.get(i));
            }
            else if(fallingBeatsListX.get(i) == downArrowButtonX) {
                text("⇓", downArrowButtonX, fallingBeatsListY.get(i));
            }
            else if(fallingBeatsListX.get(i) == upArrowButtonX) {
                text("⇑", upArrowButtonX, fallingBeatsListY.get(i));
            }
            else if(fallingBeatsListX.get(i) == rightArrowButtonX) {
                text("⇒", rightArrowButtonX, fallingBeatsListY.get(i));
            }

            textSize(50);

            float yCoord = fallingBeatsListY.get(i);
            yCoord+=noteFallSpeed;
            fallingBeatsListY.set(i, yCoord);

            if(fallingBeatsListY.get(i) > height/1.39f && fallingBeatsListY.get(i) < height/1.39f+noteFallSpeed*4.5f) {

                if(fallingBeatsListX.get(i) == leftArrowButtonX && leftPressed == false) {
                    scoreMultiplier = 1;
                    noteStreak = 0;
                    sat = 70;
                    missedNotes++;
                }

                else if(fallingBeatsListX.get(i) == downArrowButtonX && downPressed == false) {
                    scoreMultiplier = 1;
                    noteStreak = 0;
                    sat = 70;
                    missedNotes++;
                }

                else if(fallingBeatsListX.get(i) == upArrowButtonX && upPressed == false) {   
                    scoreMultiplier = 1;
                    noteStreak = 0;
                    sat = 70;
                    missedNotes++;
                }

                else if(fallingBeatsListX.get(i) == rightArrowButtonX && rightPressed == false) {
                    scoreMultiplier = 1;
                    noteStreak = 0;
                    sat = 70;
                    missedNotes++;
                }
                    
                textSize(50);

            }

            if(leftPressed == true && fallingBeatsListY.get(i) > height/1.48f && fallingBeatsListY.get(i) <= height/1.39f &&  fallingBeatsListX.get(i) == leftArrowButtonX)
            {
                noteHit = true;
            }


            if(downPressed == true && fallingBeatsListY.get(i) > height/1.48f && fallingBeatsListY.get(i) <= height/1.39f && fallingBeatsListX.get(i) == downArrowButtonX)
            {
                noteHit = true;
            }

            if(upPressed == true && fallingBeatsListY.get(i) > height/1.48f && fallingBeatsListY.get(i) <= height/1.39f && fallingBeatsListX.get(i) == upArrowButtonX)
            {
                noteHit = true;
            }

            if(rightPressed == true && fallingBeatsListY.get(i) > height/1.48f && fallingBeatsListY.get(i) <= height/1.39f && fallingBeatsListX.get(i) == rightArrowButtonX)
            {
                noteHit = true;
            }
                    
            if(noteHit == true)
            {
                fallingBeatsListY.remove(i);
                fallingBeatsListX.remove(i);
                scoreMultiplier = scoreMultiplier + 0.05f;
                score = round(score+100*scoreMultiplier);
                noteStreak++;
                hitNotes++;
                noteHit = false; 
                sat+=15;
            }
        }
    }
}

    void mainMenu()
    {
        //If no song is playing, then the game is not running, so main menu is displayed.
        if(!audioPlayerAlt.isPlaying() && !getAudioPlayer().isPlaying())
        {
            cursor(ARROW);
            difficultySet = false;
            background(0);

            scoreMultiplier = 1;
            noteStreak = 0;
            score = 0;
            sat = 70;
            missedNotes = 0;
            hitNotes = 0;

            getAudioPlayer().pause();

            String song1 = "Alibi";
            String song2 = "Heavyweight";
            String song3 = "Cradles";
            String song4 = "Fearless";

            float song1ButtonY = height/2-80;
            float song2ButtonY = height/2-20;
            float song3ButtonY = height/2+40;
            float song4ButtonY = height/2+100;

            int buttonTextSize = 45;

            fallingBeatsListX.clear();
            fallingBeatsListY.clear();


            if(hue <= 360)
            {
                hue ++;
            }
            else
            {
                hue = 0;
            }

            fill(hue, 360, 360);

            textAlign(CENTER, TOP);
            textSize(90);
            text("BEATMAProcessing", width/2, 0);
            textSize(60);
            fill(255);
            text("SONG SELECTION", width/2, height/2-height/4);
            textSize(buttonTextSize);
            textAlign(CENTER, CENTER);
            text(song1, width/2, song1ButtonY);
            text(song2, width/2, song2ButtonY);
            text(song3, width/2, song3ButtonY);
            text(song4, width/2, song4ButtonY);

            if(difficultySet == false) {
                stroke(255, 0, 300);
                textSize(60);
                line(0,height/2+height/4, width, height/2+height/4);
                text("DIFFICULTY", width/2, height/2+height/3);
                textSize(40);
                text("Easy", width/4*1, height/1.08f);
                text("Normal", width/4*2, height/1.08f);
                text("Hard", width/4*3, height/1.08f);
            }

            if(mousePressed)
            {
                //song1 button
                if(mouseX > width/2-400 && mouseX < width/2 + 400 && mouseY < song1ButtonY+(buttonTextSize-8) && mouseY > song1ButtonY-18)
                {
                    loadAudio(song1 + ".mp3");
                    text(">", width/4, song1ButtonY);
                    text("<", width-width/4, song1ButtonY);
                    audioPlayerAlt = minimAlt.loadFile(song1 + ".mp3");
                    timer2 = frameCount;
                    audioPlayerAlt.play();
                    audioPlayerAlt.mute();
                }
            
                //song2 button
                if(mouseX > width/2-400 && mouseX < width/2 + 400 && mouseY < song2ButtonY+(buttonTextSize-8) && mouseY > song2ButtonY-18)
                {
                    loadAudio(song2 + ".mp3");
                    text(">", width/4, song2ButtonY);
                    text("<", width-width/4, song2ButtonY);
                    audioPlayerAlt = minimAlt.loadFile(song2 + ".mp3");
                    timer2 = frameCount;
                    audioPlayerAlt.play();
                    audioPlayerAlt.mute();
                }

                //song3 button
                if(mouseX > width/2-400 && mouseX < width/2 + 400 && mouseY < song3ButtonY+(buttonTextSize-8) && mouseY > song3ButtonY-18)
                {
                    loadAudio(song3 + ".mp3");
                    text(">", width/4, song3ButtonY);
                    text("<", width-width/4, song3ButtonY);
                    audioPlayerAlt = minimAlt.loadFile(song3 + ".mp3");
                    timer2 = frameCount;
                    audioPlayerAlt.play();
                    audioPlayerAlt.mute();
                }

                //song4 button
                if(mouseX > width/2-400 && mouseX < width/2 + 400 && mouseY < song4ButtonY+(buttonTextSize-8) && mouseY > song4ButtonY-18)
                {
                    loadAudio(song4 + ".mp3");
                    text(">", width/4, song4ButtonY);
                    text("<", width-width/4, song4ButtonY);
                    audioPlayerAlt = minimAlt.loadFile(song4 + ".mp3");
                    timer2 = frameCount;
                    audioPlayerAlt.play();
                    audioPlayerAlt.mute();
                }
            
                if(difficultySet == false) {
                    //Easy difficulty button
                    if(mouseX > width/6 && mouseX < width/6*2) {
                        beatDetector.setSensitivity(500);
                        text(">", width/6, height/1.08f);
                        text("<", width/6*2, height/1.08f);
                        difficultySet = true;
                    }
                    //Normal difficulty button
                    if(mouseX > width/6*2.5f && mouseX < width/6*3.5f && mouseY > height/1.2f) {
                        beatDetector.setSensitivity(390);
                        text(">", width/6*2.5f, height/1.08f);
                        text("<", width/6*3.5f, height/1.08f);
                        difficultySet = true;
                    }
                    //Hard difficulty button
                    if(mouseX > width/6*4 && mouseX < width/6*5 && mouseY > height/1.2f) {
                        beatDetector.setSensitivity(250);
                        text(">", width/6*4, height/1.08f);
                        text("<", width/6*5, height/1.08f);
                        difficultySet = true;
                    }
                }

            }

        }
    }



    void runGame()
    {
        //Check if music is playing, and if it is, run the game!
        if(audioPlayerAlt.isPlaying() || getAudioPlayer().isPlaying())
        {
            noCursor();
            
            background(0);

            //Start of game
           
            if(hue <= 360) {
                hue ++;
            }
            else {
                hue = 0;
            }

            float accuracy = (hitNotes+missedNotes);
            accuracy = hitNotes/accuracy*100;
            fill(hue, sat, sat+100);
            textSize(33);

            //text("Accuracy: " + round(accuracy) + "%", width-width/2f, height/20*18);
            text("Streak: " + noteStreak, width/2, height/20*19);
            text("Score: " + round(score) + " (x" + df.format(scoreMultiplier) + ")", width/2f, height/20*18);

            //Draw buttons player sees
            drawButtonBar();

            //Mutes audio player used to detect beats

            //CALLED AT START OF GAME, WHERE BEAT DETECTOR IS RUNNING BUT MUSIC IS NOT
            if (getAudioPlayer().isPlaying() == false && frameCount - timer2 >= 180)
            { 
                getAudioPlayer().play();
            }

            //CALLED BEFORE AUDIO STARTS PLAYING
            if(getAudioPlayer().isPlaying() == false && frameCount - timer2 < 40) 
            {
                fill(255);
                text("PRESS 'SPACE' TO RETURN TO MENU", width/2, height/2-(height/4));

            }
           
            //VISUALISER
            for(int j = 0; j < getFFT().specSize(); j++)
            {
                //draw the line for frequency band i, scaling it up a bit so we can see it
                line(j*2.81f, height - getFFT().getBand(j)*12, j*2.81f, height - getFFT().getBand(j)*21);
                line(width - j*2.81f, height - getFFT().getBand(j)*12, width - j*2.81f, height - getFFT().getBand(j)*21);
            }

            //End of game

        }
    }

    void drawButtonBar()
    {
        //Button control positions (arrow keys)
        textSize(250);

        int leftArrowButtonX = width/2-225;//-225
        int downArrowButtonX = width/2-75;//-75
        int upArrowButtonX = width/2+75;//+75
        int rightArrowButtonX = width/2+225;//+225

        fill(360, 0, leftControlFill);
        text("⇐", leftArrowButtonX, height/1.45f);
        fill(360, 0, downControlFill);
        text("⇓", downArrowButtonX, height/1.45f);
        fill(360, 0, upControlFill);
        text("⇑", upArrowButtonX, height/1.45f);
        fill(360, 0, rightControlFill);
        text("⇒", rightArrowButtonX, height/1.45f);

        if(getAudioPlayer().isPlaying() == true && frameCount - timer > 3f)
        {

            leftControlFill = 100;
            downControlFill = 100;
            upControlFill = 100;
            rightControlFill = 100;
            leftPressed = false;
            downPressed = false;
            upPressed = false;
            rightPressed = false;

        }

        textSize(50);

    }

    void detectBeats()
    {
        beatDetector.detect(audioPlayerAlt.mix);

        fill(255, 255, 255);
        
        if (beatDetector.isOnset()) {
            generateFallingBeat();
        }

    }

    void generateFallingBeat() 
    {
        
        int leftArrowButtonX = width/2-225;//-225
        int downArrowButtonX = width/2-75;//-75
        int upArrowButtonX = width/2+75;//+75
        int rightArrowButtonX = width/2+225;//+225

        int randNum = (int)(Math.random() * ((4 - 1) + 1)) + 1;

        switch(randNum) {
            case (1): //Left
                selectedX = leftArrowButtonX;
                fallingBeatsListX.add(selectedX);
                fallingBeatsListY.add(selectedY);
                break;
            case (2): //Down
                selectedX = downArrowButtonX;
                fallingBeatsListX.add(selectedX);
                fallingBeatsListY.add(selectedY);
                break;
            case (3): //Up
                selectedX = upArrowButtonX;
                fallingBeatsListX.add(selectedX);
                fallingBeatsListY.add(selectedY);
                break;
            case (4): //Right
                selectedX = rightArrowButtonX;
                fallingBeatsListX.add(selectedX);
                fallingBeatsListY.add(selectedY);
                break;
        }


    }


}
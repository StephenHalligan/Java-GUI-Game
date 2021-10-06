# Instructions

This game is fairly self explanitory - first, you select the difficulty. The game has 3 different difficulty settings - easy, normal and hard. 

1. 'Easy' is recommended for anyone who with subpar reaction times, or if playing the game in a non-intense environment.
2. I would recommend the 'Normal' difficulty for anyone with experience in beat games, or someone looking for a bit more of a challenge than easy mode. Normal is the default difficulty.
3. 'Hard' generates more beats than easy or normal difficulties. Also, the interval between the beats is much shorter - quick reaction times are recommended, a real challenge!

Once you have selected the difficulty by clicking on it, you select your song by clicking on the song name. There are currently 4 different songs in the game, 'Alibi', 'Heavyweight', 'Cradles' and 'Fearless'. All of the songs are provided by NoCopyrightSounds.

Upon clicking on the song name to select it, the game starts! As beats are generated, they fall down the screen towards the grey arrow keys, or player controls. The player's goal is to press the arrow keys on their keyboard that correspond with the note that is falling onto the key. If they get the timing right, and press the correct key as the beat falls directly onto it, they score. Hitting multiple beats in a row *increases your 'Streak' and 'Multiplier' counters*, which increses your score! Hitting notes increses the vibrancy of the game board - saturating the notes and creating a beautifully bright rainbow effect!

If they get the timing wrong however, the beat falls past the controls and counts as a miss. When the player misses a beat, their score multiplier is reset to 1, and their streak is reset to 0. Upon missing, the saturation of the notes fades away, creating a bleak and grey game board. Regain a streak to increase vibrancy again!

---

# How it works

The game works mostly using the processing minim library. For the project, I have used 2 different audioplayers. By doing this, it allows me to have 1 audio player which 'reads' the track's beats using *minim's 'BeatDetect' algorithm*. This track is not heard by the player (muted). The 2nd audio player is the track which the player hears, and is played through their speakers/headphones etc. at a set interval *AFTER* the 1st audio player. This gives the 1st audio player time to generate beats using our beatdetector, and drop them from the top of the screen. This gives the player time to react to the falling beats, and ensures that the beats hit the player control arrow keys to sync with the beat of the music!

As described in the instructions, the rainbow effect is powered by the hue of the text/strokes. The saturation of the game board is powered by the text etc. saturation, and is changed based on how many notes are hit in a row, or if the player misses a note.

I also use an audio visualiser to add some 'jazz' to the game. The visualisers on either sides of the player's arrow keys react to the 2nd audio player, or in other words, react to the music that the player actually hears.

---


```Java
            if (getAudioPlayer().isPlaying() == false && frameCount - timer2 >= 180)
            { 
                getAudioPlayer().play();
            }
```
	^ Example of the frameCount timer which starts the 2nd audio player

There are many little things in the project that I am quite proud of also, such as the little touches like the difficulty setting making use of the *setSensitivity* function (part of BeatDetect) to increase/decrease the sensitivity of the Beat detector algorithm.

```Java
                    //Easy difficulty button
                    if(mouseX > width/6 && mouseX < width/6*2) {
                        beatDetector.setSensitivity(500);
                        text(">", width/6, height/1.08f);
                        text("<", width/6*2, height/1.08f);
                        difficultySet = true;
                    }
```
	^ Example of the difficulty selector using the BeatDetect setSensitivity function

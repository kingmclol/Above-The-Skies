import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The introduction world with the introduction cutscene.
 * Press enter to skip.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-16
 */
public class IntroWorld extends WorldWithMusic
{
    SimpleTimer textTimer; // Timer for displaying text
    Label textLabel; // Text label for dialogue
    Label skipLabel; // Tells player they can skip this
    String[] dialogue; // Dialogue list
    int dialogueIndex; // Current dialogue line
    Player examplePlayer; // The example player
    public IntroWorld()
    {
        transition("FADE_OUT");
        songList = new GreenfootSound[]{
            new GreenfootSound("Hitman.mp3"),
            new GreenfootSound("My_Dark_Passenger.mp3"),
            new GreenfootSound("Chase.mp3")
        };
        songIndex = Greenfoot.getRandomNumber(songList.length); // Get a randomm song index to start with.
        currentSong = songList[songIndex]; // Set the current song to the song index.
        
        cloudTimer = new SimpleTimer(); // Using clouds, so create a cloudTimer.
        textTimer = new SimpleTimer();
        dialogueIndex = 0;
        prepare();
    }
    public void prepare() {
        setSkyBackground();
        examplePlayer = new Player(); 
        addObject(examplePlayer, getWidth()/2, getHeight()-150);
        
        skipLabel = new Label("[ENTER] to skip.", 24);
        skipLabel.setFillColor(Color.BLACK);
        addObject(skipLabel, skipLabel.getImage().getWidth()/2+10, getHeight()-skipLabel.getImage().getHeight()+10);
        
        textLabel = new Label ("If you see this text, something\nbroke. Just skip.", 32); // The inital string value is only shown for one frame anyways, so it doesn't matter what's in here basically.
        textLabel.setFillColor(Color.BLACK);
        addObject(textLabel, getWidth()/2, 200);
        dialogue = new String[] { // The dialogue lines.
            "Epsilon-3! Do you copy?",
            "You're nearby Site Alpha-H5, right?",
            "Reports say that something strange\nis happening nearby the area.",
            "Another alien invasion, apparently.",
            "Normally, we can easily push them back,\nbut this time, we're simply not prepared.",
            "Epsilon-3, you're up for the job.\nHold out for as long as you can.",
            "We both know this is a death sentence,\nthat you will likely die.",
            "But every second, every kill,\nwill give reinforcements precious time.",
            "It may be overwhelming fending them off\nalone, but we believe in you.",
            "Remember, as a GUARDIAN, your job is to...",
            "\"Fight for the people,\n                 Above the Skies.\"",
            "Good luck, Epsilon-3, and goodbye."
        };
    }
    public void act() { 
        checkSongState();
        
        spawnCloud();
        
        say(dialogue[dialogueIndex], 5000);
        
        if (Greenfoot.isKeyDown("enter")) {
            transition();
        }
    }
    /**
     * Stops the current song and sets the world to GameWorld.
     */
    public void goToNextWorld() {
        currentSong.stop();
        Greenfoot.setWorld(new GameWorld());
    }
    /**
     * Displays the text on the center of the screen.
     */
    private void say(String text, int delay) {
        textLabel.setValue(text);
        if (textTimer.millisElapsed() >= delay) { // Delay period is over.
            if (dialogueIndex == dialogue.length-1) { // The final line is currently shown, so
                transition(); // Transition to the next world.
            }
            else { // There is still dialogue to show,
                textTimer.mark(); // Reset the timer.
                dialogueIndex = Math.min(dialogueIndex+1, dialogue.length-1); // Increase the dialogueIndex by one, not exceeding the max index of the dialgue array.
            }
        }
    }
}

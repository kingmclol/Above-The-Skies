import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The End. Shows the score on screen.
 * Pressing the enter key will go back to the GameWorld.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-13
 */
public class EndWorld extends WorldWithMusic
{
    private int score; // The score.
    private int millisSurvived; // How long the player survived for.
    private Label scoreLabel; // Label for showing score.
    private Label endLabel; // Tells the player how well they did.
    private Label instruction; // Tells the player that they can play again.
    private final String WIN_ABSOLUTE; // You soloed the enemy and survived.
    private final String WIN_SACRIFICE; // You did well enough until reinforcements arrived.
    private final String NEUTRAL_VICTORY; // You did enough to not have the world die, but the consequences were dire.
    private final String NEUTRAL_PACIFIST; // You simply watched the enemy and tried to buy time, without attacking at all. Okay?
    private final String LOSE_DESTRUCTION; // You didn't hold out long enough. The world is doomed.
    /**
     * Creates an ending world.
     * 
     * @param score             The player's score.
     * @param millisSurvived    How long the player survived, in milliseconds
     */
    public EndWorld(int score, int millisSurvived)
    {    
        super();
        this.score = score;
        this.millisSurvived = millisSurvived;
        // Create the songList.
        songList = new GreenfootSound[]{
            new GreenfootSound("Wallpaper.mp3")
        };
        songIndex = Greenfoot.getRandomNumber(songList.length); // Get a random song index from songList. Wait, there's only one...
        currentSong = songList[songIndex]; // Set the current song based on the songIndex.
        cloudTimer = new SimpleTimer(); // Using clouds, so create a cloudTimer.
        
        // Searched up how to do multi line strings, this was an option. Seems like it does something like
        // adding the first parameter between every next parameter. But hey, it works!
        WIN_ABSOLUTE = String.join("\n", // Added between each parameter, I think. Kinda like a multiline string.
                                "Epsilon-3! Reinforcements have arrived!",
                                "Wait... where are the aliens?",
                                "Oh my god... Epsilon-3...",
                                "Epsilon-3, are you even human?",
                                "How the hell did you even DO that?",
                                "------------------------------------------------------",
                                "Maybe you should take a break. After all,",
                                "Nothing is getting past YOU.");
        WIN_SACRIFICE = String.join("\n", 
                                "On this day,",
                                "we remember a hero.",
                                "A brave soul who saved us all.",
                                "Alone they fought, in the never-ending hell,",
                                "A savior, an angel, a Guardian.",
                                "Epsilon-3, may your soul be ever flying",
                                "\n\nAbove the Skies.",
                                "------------------------------------------------------",
                                "Thank you for playing this game.");
        NEUTRAL_VICTORY = String.join("\n", 
                                "Epsilon-3, you did well. But not well enough.",
                                "The invaders were pushed back. We won.",
                                "Yeah, as if you can count this as a victory.",
                                "Well, at least it was better than the alternative.",
                                "If only we could try again...");
        NEUTRAL_PACIFIST = String.join("\n", 
                                "Okay, very impressive. Probably.",
                                "But why? Even if you cheated,",
                                "It takes 3 minutes to do this...",
                                "Was the game too easy?",
                                "Or did you just use the \"[\" key?",
                                "Or did you simply pause the execution?",
                                "But do what you want... I dunno.");
        LOSE_DESTRUCTION = String.join("\n", 
                                "Well, well, well.",
                                "What do we have here?",
                                "We thought this world was completely destroyed.",
                                "Seems like we weren't too exhaustive in our search.",
                                "Be warned, stranger.",
                                "You   c a n n o t   hide.");
        prepare();
    }
    protected void prepare() {
        transition("FADE_OUT");
        setSkyBackground();
        
        scoreLabel = new Label(String.format("Score: %d%nTime Survived: %d seconds", score, millisSurvived/1000), 36);
        scoreLabel.setFillColor(Color.BLACK);
        addObject(scoreLabel, getWidth()/2, 40); 
        
        instruction = new Label("Press [ENTER] to play again.", 36);
        instruction.setFillColor(Color.BLACK);
        addObject(instruction, getWidth()/2, getHeight()-instruction.getImage().getHeight());
        if (score >= 50000) { // You literally defended yourself from the entire alien army.
            GreenfootImage background = new GreenfootImage(getWidth(), getHeight());
            background.setColor(new Color(212, 114, 99));
            background.fill();
            setBackground(background);
            endLabel = new Label(WIN_ABSOLUTE, 24);
        }
        else if (score == 0 && millisSurvived >= 180000) { // Survived more than 180 seconds, without killing any enemy.
            // It's probably not the most difficult as early-spawned enemies have weaker stats than those spawned later in the game.
            endLabel = new Label(NEUTRAL_PACIFIST, 24);
        }
        else if (score >= 10000 && millisSurvived >= 120000) { // More than ~50 enemies killed AND more than 120 seconds survived. Win.
            endLabel = new Label(WIN_SACRIFICE, 24);
        }
        else if (score >= 5000 || millisSurvived >= 60000) { // More than 60 seconds survived, OR 5000 score (~25 kills). Pyrrhic victory.
            endLabel = new Label(NEUTRAL_VICTORY, 24);
        }
        else { // Didn't do well enough. Lose.
            endLabel = new Label(LOSE_DESTRUCTION, 24);
        }
        endLabel.setFillColor(Color.BLACK);
        addObject(endLabel, getWidth()/2, getHeight()/2);
    }
    public void act() {
        checkSongState();
        spawnCloud();
        if (Greenfoot.isKeyDown("enter")) {
            transition(); // Go to the next world.
        }
    }
    /**
     * Stops the current song and goes to the GameWorld.
     */
    protected void goToNextWorld() {
        currentSong.stop();
        Greenfoot.setWorld(new GameWorld());
    }
}

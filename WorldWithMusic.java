import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A world with basic background music functionality.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-13
 */
public abstract class WorldWithMusic extends World
{

    /**
     * Constructor for objects of class WorldWithMusic.
     * 
     */
    
    protected int songIndex;
    protected GreenfootSound[] songList;
    protected GreenfootSound currentSong;
    protected SimpleTimer cloudTimer;
    public WorldWithMusic()
    {    
        // Create a new world with 600x900 cells with a cell size of 1x1 pixels.
        super(600, 900, 1); 
        
        // Force the speed to be 50 or things go wrong...
        // I dug myself into a hole because I used timers rather than frames, and it's
        // too late to change it now...
        Greenfoot.setSpeed(50);
        
        // Set paint order.
        setPaintOrder(Transition.class, Label.class, StatBar.class, Player.class, Enemy.class, Projectile.class, Laser.class, Powerup.class, Particle.class, Cloud.class);
    }
    /**
     * When the world is started, play the current song.
     */
    public void started() {
        currentSong.play(); // Start the song.
    }
    /**
     * When the world is stopped, pause the current song.
     */
    public void stopped() {
        currentSong.pause(); // Pause the song.
    }
    /**
     * Default prepare method. I don't think it does anything, but I'm leaving it here just in case.
     */
    protected void prepare() {
        transition("FADE_IN"); // World fades in.
    }
    /**
     * Check if the current song is playing.
     */
    protected void checkSongState(){
        if (!currentSong.isPlaying()) { // If the current song has finished
            cycleNextSong(songList); // Play next one
        }
    }
    /**
     * Plays the next song in the [songList].
     * 
     * @param songList  The songList to cycle
     */
    protected void cycleNextSong(GreenfootSound[] songList) { 
        currentSong.stop();
        songIndex++;
        if (songIndex > songList.length-1) { // If index exceeds length
            songIndex = 0; // Cycle back to start
        }
        currentSong = songList[songIndex]; // Get new current song.
        currentSong.play();
    }
    /**
     * Swap the current playing songList to the [newSongList].
     * 
     * @param newSongList   The songList to swap to.
     */
    protected void swapPlaylist(GreenfootSound[] newSongList) {
        if (songList == newSongList) return; // If songList and newSongList have same references (same lists) do nothing;
        currentSong.stop();
        songList = newSongList; // Make songList be the newSongList
        
        // Get new random song
        songIndex = Greenfoot.getRandomNumber(songList.length);
        currentSong = songList[songIndex];
        currentSong.play(); // Play new song
    }
    /**
     * Sets the background to the sky blue colour.
     */
    protected void setSkyBackground() {
        GreenfootImage background = new GreenfootImage(getWidth(), getHeight()); // Create image of world size.
        background.setColor(new Color(135, 211, 248)); // Sky blue-ish.
        background.fill(); // Fill with sky blue colour.
        setBackground(background);
    }
    /**
     * Spawns a cloud.
     */
    protected void spawnCloud() {
        if (cloudTimer.millisElapsed() >= 1000) {
            cloudTimer.mark();
            addObject(new Cloud(), Greenfoot.getRandomNumber(getWidth()), 0);
        }
    }
    /**
     * Fades the world to black.
     */
    protected void transition() {
        transition(1000, 10, "FADE_IN");
    }
    /**
     * Adds a transition that changes type depending on [type].
     * Can only be "FADE_IN" or "FADE_OUT".
     * 
     * @param type  The type of the transition to run.
     */
    protected void transition(String type) {
        transition(1000, 10, type);
    }
    /**
     * More customizable transition.
     * [type] can only be "FADE_IN" or "FADE_OUT".
     * 
     * @param lifespan  How long it takes for the transition to complete.
     * @param increment The step between transparency adjustments.
     * @param type      The type of the transition to run.
     */
    protected void transition(int lifespan, int increment, String type) {
        Transition transition = new Transition(lifespan, increment, type);
        addObject(transition, getWidth()/2, getHeight()/2);        
    }
    /**
     * Rounding to the nearest integer. I should've added a utility class for this instead.
     * 
     * @param x The value to round
     * @return  The rounded integer value.
     */
    protected int round(double x) {
        return (int)(x+0.5);
    }
    /**
     * Abstract method for subclasses to implement what to do when progressing
     * to the next world.
     */
    protected abstract void goToNextWorld();
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Arrays; // I don't think this is used but leaving it here nonetheless.
/**
 * The Main game, where the player fights off a never-ending alien horde.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-15
 */
public class GameWorld extends WorldWithMusic
{
    private Player player; // The player.
    private int score; // The player's score.
    private GreenfootSound[] combatSongs; // Songs to play during combat.
    private GreenfootSound[] interludeSongs; // Songs to play during cutscenes/interludes. [NOT IMPLEMENTED]
    private GreenfootSound[] bossSongs; // Songs to play during boss fight. [NO BOSS EXISTS]
    SimpleTimer gameTimer; // Timer for how long the player survived.
    
    private boolean autoHeal = false; // If true, heals the player a lot every act cycle. You should ignore this, as it's just for testing. 
                                      // Yes, I know it should be deleted, but what if I want to test something else in the future?
    public GameWorld()
    {    
        super();
        // Initialize the various song lists with their songs
        combatSongs = new GreenfootSound[]{
            new GreenfootSound("Timeless.mp3"),
            new GreenfootSound("Last_Hero.mp3")
        };
        for (GreenfootSound song : combatSongs) { // For each song in combatSongs,
            song.setVolume(85); // Set volume to 85.
        }
        interludeSongs = new GreenfootSound[]{
            new GreenfootSound("Hitman.mp3"),
            new GreenfootSound("My_Dark_Passenger.mp3")
        };
        for (GreenfootSound song : interludeSongs) { // For each song in interludeSongs,
            song.setVolume(90); // Set volume to 90.
        }
        bossSongs = new GreenfootSound[]{
            new GreenfootSound("Charlotte.mp3"),
            new GreenfootSound("Duel.mp3"), 
        };
        for (GreenfootSound song : bossSongs) { // For each song in bossSongs,
            song.setVolume(70); // Set volume to 70.
        }
        cloudTimer = new SimpleTimer(); // Using clouds, so create a cloudTimer.
        gameTimer = new SimpleTimer();
        score = 0;
        songList = combatSongs; // Start with the combatSongs.
        songIndex = Greenfoot.getRandomNumber(songList.length); // Get a random index.
        currentSong = songList[songIndex]; // Get the song tied to the songIndex.
        prepare();
    }
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    protected void prepare()
    {
        transition("FADE_OUT");
        
        setSkyBackground();
        
        player = new Player();
        addObject(player,getWidth()/2,getHeight()-150);
    }
    /**
     * When the world is started, resume the music AND all the laser sounds.
     */
    public void started() {
        super.stopped();
        for (Laser laser : getObjects(Laser.class)) { // For al lasers in world,
            laser.continueSound(); // Continue its sound.
        }
    }
    /**
     * When the world is stopped, pause the music AND all the laser sounds.
     */
    public void stopped() {
        super.stopped();
        for (Laser laser : getObjects(Laser.class)) { // For all lasers in world,
            laser.pauseSound(); // pause its sound.
        }
    }
    public void act() {
        checkSongState();
        
        spawnCloud();
        
        // If less than 10 enemies in world, and RNG permits, spawn an enemy.
        if (getObjects(Enemy.class).size() < 10 && Greenfoot.getRandomNumber(100) == 0) spawnEnemy();
        
        // ---------------------------------------------TESTING STUFF, BEGIN---------------------------------------
        // Testing out some things. Just ignore it, please.
        if (autoHeal) player.heal(100000); // Heal the player a lot.
        String key = Greenfoot.getKey();
        if ("3".equals(key)) {
            songList = bossSongs; // Swap the songList to the boss songs.
            cycleNextSong(songList);
        }
        else if ("2".equals(key)) {
            songList = interludeSongs; // Swap to the interlude songs.
            cycleNextSong(songList);
        }
        else if ("1".equals(key)) {
            songList = combatSongs; // Swap to the combat songs.
            cycleNextSong(songList);
        }
        else if ("[".equals(key)) {
            // Toggles the autoHealing, basically makes you invincible. Unless you take a lot of damage in a single frame.
            // Unlikely, but it's possible.
            autoHeal = !autoHeal;
        }
        else if ("]".equals(key)) {
            // Absolutely DEMOLISH the player. Or the shield. Or nothing, if you're currently in a dash.
            // Either way, use this to kill yourself to go to the EndWorld.
            player.hit(Integer.MAX_VALUE);
        }   
        // ---------------------------------------------TESTING STUFF, END-----------------------------------------
    }
    /**
     * Spawns an enemy with increasing stats depending on how long the game has run for.
     */
    private void spawnEnemy() {
        int x = gameTimer.millisElapsed();
        int enemyHealth = round(0.0003*x+30); // Slowly increase enemy hp with time, starts at 30, no cap
        int enemyShootRate = Math.max(50, round(-0.0002*x+100)); // Lowest at 50, starts at 100
        int enemyBulletSpeed = 10;
        int enemyAccuracy = 10;
        int enemyDamage = round(0.0002*x+10); // increase enemy damage with time, no cap
        int enemyMaxSpeed = Math.min(6, round(0.00004*x+2)); // increase enemy speed, min 2, max 6
        addObject(new Enemy(enemyHealth, enemyShootRate, enemyBulletSpeed, enemyDamage, enemyAccuracy, enemyMaxSpeed), Greenfoot.getRandomNumber(getWidth()), 50);
    }
    /**
     * Stops the currently playing song and goes to the EndWorld.
     */
    public void goToNextWorld() {
        currentSong.stop();
        for (Laser laser : getObjects(Laser.class)) { // For all lasers in world,
            laser.pauseSound(); // Stop its sound.
        }
        Greenfoot.setWorld(new EndWorld(score, gameTimer.millisElapsed())); // Set the world to a new Endworld, giving it the score, and the time survived.
    }
    /**
     * Adds [gain] score to the score count.
     * 
     * @param gain  The amount of score that is gained.
     */
    public void addScore(int gain) {
        score += gain;
        if (score >= 50000) transition(); // You win. Go to the end world. Please take a break.
    }
    /**
     * Removes [loss] score to the score count. Does not go below zero, where 0 will be returned.
     * 
     * @param loss  The amount of score that is lost.
     */
    public void loseScore(int loss) {
        score = Math.max(0, score-loss);
    }
    /**
     * Returns a reference to the player.
     * 
     * @return  A reference to the player
     */
    public Actor getPlayer() {
        return player;
    }
    
}

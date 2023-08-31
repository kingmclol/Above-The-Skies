import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player the player plays as.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-16
 */
public class Player extends SmoothMover
{
    private GifImage playerGif, playerGif_normal, playerGif_shielded;
    
    private final int BULLET_ACCURACY_MIN = 4; // Maximum bullet accuracy.
    private final int ROLL_COOLDOWN_MIN = 1000; // Minimum roll cooldown, in milliseconds.
    private final int ROUNDS_PER_MINUTE_MAX = 1000; // Maximum RPM (firerate) of player.
    private final int BULLET_DAMAGE_MAX = 20; // Maximum bullet damage.
    private final int SHIELD_HEALTH_MAX = 500; // Maximum shield hitpoints.
    private final int BULLET_SPEED_MAX = 15; // Maximum bullet speed.
    private double velX, velY; // The X and Y speed of the player.
    private double accel; // The accelration of the player.
    private int maxHealth, maxShield; // The Health and Shield of the player.
    private int shieldRegenRate; // How much shield is regenerated every frame.
    private SimpleTimer shotTimer, rollTimer; // Timers to control shot and roll cooldowns
    private int roundsPerMin; // The rounds per minute (RPM) or firerate of the player.
    private int rollCooldown; // The cooldown beteeen rolls, in miliseconds.
    private int bulletSpeed; // The bullet speed.
    private int bulletDamage; // The bullet damage.
    private int bulletAccuracy; // The player's accuracy when firing. 0 is 100% accuracy, higher is less accurate.
    private final double decay; // THE % OF SPEED RETAINED AFTER ONE CYCLE. AFFECTS MAX SPEED AND STUFF
    private boolean immune; // Determines if the player can recieve damage.
    private boolean shielded; // Determines if damage should be redirected to the shield.
    private BetterHealthBar healthBar; // The player's healthbar.
    private ShieldBar shieldBar; // The player's shieldbar.
    private StatBar cooldownBar; // The cooldown bar for rolls.
    private String dashKey; // The key used to roll.
    private String[] shieldKeys; // The keys used to activate the shield.
    private GreenfootSound hitSound, deathSound, shootSound; // Sounds.
    private GreenfootSound shieldHitSound, shieldBrokenSound; // Shield sounds.
    /**
     * Creates a player.
     */
    public Player() {
        velX = 0;
        velY = 0;
        accel = 1;
        decay = 0.8;
        
        bulletSpeed = 10;
        bulletDamage = 10;
        bulletAccuracy = 10;
        roundsPerMin = 500; 
        rollCooldown = 2000;
        maxHealth = 1000;
        maxShield = 250;
        shieldRegenRate = 1;
        
        immune = false; 
        playerGif_normal = new GifImage("Plane_v3.gif");
        playerGif_shielded = new GifImage("Plane_Shielded_v1.gif");
        
        playerGif = playerGif_normal;
        setImage(playerGif.getCurrentImage());
        shotTimer = new SimpleTimer();
        rollTimer = new SimpleTimer();
        healthBar = new BetterHealthBar(this, 2, maxHealth, false);
        shieldBar = new ShieldBar(this, 2, maxShield, false);
        cooldownBar = new StatBar(this, 50, 5, rollCooldown, rollTimer.millisElapsed(), Color.YELLOW, Color.GRAY, true, true);
        
        dashKey = "shift";
        shieldKeys = new String[]{"tab","j"};
        
        shootSound = new GreenfootSound("Player_Shoot.mp3");
        shootSound.setVolume(30);
        
        hitSound = new GreenfootSound("Player_Hit.mp3");
        hitSound.setVolume(50);
        
        deathSound = new GreenfootSound("Death.mp3");
        deathSound.setVolume(60);
        
        shieldHitSound = new GreenfootSound("Shield_Hit.mp3");
        shieldHitSound.setVolume(50);
        
        shieldBrokenSound = new GreenfootSound("Shield_Broken.mp3");
        shieldBrokenSound.setVolume(70);
        }
    /**
     * Spawns in the various StatBars tied to the player (health, shield, rollCooldown).
     * Only spawns the health and shield bars if spawned in a GameWorld.
     */
    public void addedToWorld(World world) {
        world.addObject(cooldownBar, getX(), getY()); // You get the okay since hidden at max, and non-obstructive
        if (world instanceof GameWorld) { // ONLY spawn in health and shield bar when in GameWorld 
                world.addObject(healthBar, world.getWidth()/2+healthBar.getImage().getWidth()/4, world.getHeight()-75);
                world.addObject(shieldBar, world.getWidth()/2+shieldBar.getImage().getWidth()/4, world.getHeight()-25);
        }
    }
    public void act()
    {
        if (healthBar.atZero()) { // Oh no, the player is dead!
            playSound(deathSound);
            GameWorld game = (GameWorld) getWorld(); // Get the world,
            game.transition(); // Start its transition,
            game.removeObject(this); // and remove this.
            return;
        }
        setImage(playerGif.getCurrentImage());
        movement();
        action();
        shoot();
    }
    /**
     * Manages movement of player, terrible system. Should've done something else, but this works fine enough...
     */
    private void movement() {
        if (Greenfoot.isKeyDown("w")) {
            velY -= accel;
        }
        if (Greenfoot.isKeyDown("s")) {
            velY += accel; 
        }
        if (Greenfoot.isKeyDown("a")) {
            velX -= accel;
        }
        if (Greenfoot.isKeyDown("d")) {
            velX += accel;
        }
        setLocation(getX()+velX, getY()+velY); // Set location
        velX *= decay; //Speed decay
        velY *= decay;
    }
    /**
     * Gets the action of the user; rolling or using shield.
     * Also manages cooldowns and shield regeneration.
     */
    private void action() {
        if (Greenfoot.isKeyDown(dashKey) && rollTimer.millisElapsed() >= rollCooldown) { // Roll if dashKey pressed and roll available
            rollTimer.mark();
            immune = true; // immunity
            accel = 3; //go faster
            for (GreenfootImage image : playerGif.getImages()) { // Change image transparency to slightly transparent to indicate immunity
                image.setTransparency(100);
            }
        }
        else if (rollTimer.millisElapsed() >= 200) { // End roll
            accel = 1; // back to normal acceleration
            immune = false; // can be hit again
            for (GreenfootImage image : playerGif.getImages()) { // Change image transparency back to opaque
                image.setTransparency(255);
            }
        }
        cooldownBar.setValue(rollTimer.millisElapsed()); // Sets the cooldownBar's current value to the time elapsed since the roll.
        
        if ((Greenfoot.isKeyDown(shieldKeys[0]) || Greenfoot.isKeyDown(shieldKeys[1])) && !shieldBar.atZero()) { //Shield when shield keys are pressed and shield available
            shielded = true;
            playerGif = playerGif_shielded;
        }
        else if (!(Greenfoot.isKeyDown(shieldKeys[0]) && Greenfoot.isKeyDown(shieldKeys[1])) || shieldBar.atZero()) { // Stop being shielded if shieldKeys are released or no shield left.
            shielded = false;
            playerGif = playerGif_normal;
        }
        
        if (!shielded && !(Greenfoot.isKeyDown(shieldKeys[0]) || Greenfoot.isKeyDown(shieldKeys[1]))) shieldBar.addValue(shieldRegenRate); // If the player releases the shield key and the shield is broken, slowly regen it.
    }
    /**
     * Shoots a bullet, only if a bullet is ready and space is pressed and not shielded.
     */
    private void shoot() {
        // If a bullet is ready AND the player pressed space AND the player is not shielded,
        if (shotTimer.millisElapsed() >= (60000d/roundsPerMin) && Greenfoot.isKeyDown("space") && !shielded) {
            shotTimer.mark(); // Reset the bullet cooldown.
            getWorld().addObject( // Add the bullet.
                        new PlayerBullet(bulletSpeed, bulletDamage, bulletAccuracy)
                    , getX()
                    , getY()-30);
            playSound(shootSound);
        }
    }
    /**
     * The player is hit for [damage] damage. If immune, the damage is ignored entirely. If shielded, the damage is redirected to the shieldbar.
     * Else, the damage is directed towards the healthBar's health.
     * 
     * @param damage    The damage that the play is hit for.
     */
    public void hit(int damage) {
        if (immune) return; // Nothing happens as immune
        else if (shielded) { // Shielded, shield will take damage.
            shieldBar.loseValue(damage);
            if (shieldBar.atZero()) playSound(shieldBrokenSound); // Shield at zero, play broken sound
            else playSound(shieldHitSound); // Shield hit, play hit sound
        }
        else { // The player loses the health.
            healthBar.loseValue(damage);
            playSound(hitSound);
        }
    }
    /**
     * Increases the accuracy of the player. Will not go below BULLET_ACCURACY_MIN.
     * 
     * @param increase  How much the player's accuracy increases by.
     */
    public void increaseAccuracy(int increase) {
        bulletAccuracy = Math.max(BULLET_ACCURACY_MIN, bulletAccuracy-increase);
    }
    /**
     * Decreases the roll cooldown. Will not go below ROLL_COOLDOWN_MIN.
     * 
     * @param decrease  The amount of decrease in the roll cooldown, in milliseconds.
     */
    public void decreaseRollCooldown(int decrease) {
        rollCooldown = Math.max(ROLL_COOLDOWN_MIN, rollCooldown-decrease);
        cooldownBar.setMaxValue(rollCooldown);
    }
    /**
     * Increases the RPM of the player (shooting rate). Will not go above ROUNDS_PER_MINUTE_MAX.
     * 
     * @param increase  How much RPM increase to the RPM.
     */
    public void increaseRPM(int increase) {
        roundsPerMin = Math.min(ROUNDS_PER_MINUTE_MAX, roundsPerMin+increase);
    }
    /**
     * Increases the bullet speed. Does not exceed BULLET_SPEED_MAX.
     * 
     * @param increase  How much speed increase for bullet.
     */
    public void increaseBulletSpeed(int increase) {
        bulletSpeed = Math.min(BULLET_SPEED_MAX, bulletSpeed+increase);
    }
    /**
     * Heals the player. How healthy!
     * 
     * @param health    The amount of health regenerated.
     */
    public void heal(int health) {
        healthBar.addValue(health);
    }
    /**
     * Increases the bullet damage. Does not exceed BULLET_DAMAGE_MAX.
     * 
     * @param increase  The amount of damage increase for bullets.
     */
    public void increaseDamage(int increase) {
        bulletDamage = Math.min(BULLET_DAMAGE_MAX, bulletDamage+increase);
    }
    /**
     * Increases the max shield hitpoints. Does not exceed SHIELD_HEALTH_MAX.
     * 
     * @param increase  The amount of hitpoint increase for the shield.
     */
    public void increaseMaxShield(int increase) {
        maxShield = Math.min(SHIELD_HEALTH_MAX, maxShield+increase);
        shieldBar.setMaxValue(maxShield);
    }
    /**
     * Rounding. Wait, again?
     * 
     * @param x The double that should be rounded to the nearest integer.
     * @return  The double that has been rounded to the nearest integer.
     */
    private int round(double x) {
        return (int)(x+0.5);
    }
    /**
     * Plays the given sound, stopping it first.
     */
    private void playSound(GreenfootSound sound) {
        sound.stop();
        sound.play();
    }
}

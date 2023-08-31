import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * lAZOR beam! Does periodic damage to the player, lasts for a specific lifespan, goes all the way to the end of the world.
 * 
 * @author  Freeman Wang
 * @version 2023-06-13
 */
public class Laser extends Actor
{
    private GifImage laserGif; // The laser's gif.
    private GreenfootImage image; // The image of the laser.
    private boolean canHit; // If the laser can hit the player at the moment.
    private int damage; // The laser's damage per hit.
    private int lifespan; // How long the laser lasts for, in milliseconds.
    private int delayBetweenHits; // Milliseconds between hits.
    private SimpleTimer lifeTimer, hitTimer; // Timers.
    private Actor owner; // The actor that spawned the laser.
    private GreenfootSound laserSound; // The laser's sound.
    /**
     * Creates a generic laser with 15 damage, 2000ms lifespan, 100ms between hits.
     * 
     * @param owner     The actor that fired the lazer
     */
    public Laser(Actor owner) {
        this(owner, 15, 2000, 100);
    }
    /**
     * Creates a laser
     * 
     * @param owner             The actor that fired the lasor.
     * @param dmg               How much damage the laser does every hit.
     * @param lifespan          How long the laser lasts for, in millseconds.
     * @param delayBetweenHits  Delay between hits, in milliseconds.
     */
    public Laser(Actor owner, int dmg, int lifespan, int delayBetweenHits) {
        this.owner = owner;
        this.damage = dmg;
        this.lifespan = lifespan;
        this.delayBetweenHits = delayBetweenHits;
        canHit = true;
        laserGif = new GifImage("Laser_v1.gif");
        laserSound = new GreenfootSound("Laser.mp3");
        lifeTimer = new SimpleTimer();
        hitTimer = new SimpleTimer();
    }
    public void addedToWorld(World world) {
        image = new GreenfootImage(laserGif.getCurrentImage().getWidth(), world.getHeight()); // create image of the world height
        laserSound.playLoop();
        updateImage();
    }
    public void act()
    {
        updateImage(); // Updates the image.
        collisionCheck();
        if (owner.getWorld() == null) { // If owner is dead, remove this too.
            laserSound.stop();
            getWorld().removeObject(this);            
            return;
        }
        
        if (lifeTimer.millisElapsed() >= lifespan) { // If time passed is more than lifespan, make owner move again and remove this.
            letOwnerMoveAgain();
            laserSound.stop();
            getWorld().removeObject(this);
            return;
        }
    }
    /**
     * Updates the laser image to the current image of the GIF.
     */
    private void updateImage() {
        // Basically draws repeating laser images to fill the entire image
        for (int i = 0; i < getWorld().getHeight(); i+=laserGif.getCurrentImage().getHeight()) {
            image.drawImage(laserGif.getCurrentImage(), 0, i);
        }
        // Sets the image to the image.
        setImage(image);
    }
    /**
     * Does collision checking if it hit the player. Manages the delay between hits.
     */
    private void collisionCheck() {
        Actor actor = getOneIntersectingObject(Player.class);
        if (actor != null && canHit) { // If hit something and able to hit them
            Player player = (Player)actor;
            player.hit(damage); // Hit the player.
            canHit = false; // cannot hit again for now
            hitTimer.mark();
        }
        else if (!canHit && hitTimer.millisElapsed() >= delayBetweenHits) { // if cannot hit currently and the delay between a hit has ben exceedxed
            canHit = true; // can hit again
        }
    }
    /**
     * Lets the enemy move again by calling its instance method moveAgain().
     */
    private void letOwnerMoveAgain() {
        Enemy enemy = (Enemy) owner;
        enemy.moveAgain();
    }
    /**
     * Stops the laser sound.
     */
    public void pauseSound() {
        laserSound.pause();
    }
    /**
     * Resumes the laser sound.
     */
    public void continueSound() {
        laserSound.playLoop();
    }
}

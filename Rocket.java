import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Rockets are high-damaging projectiles that home towards a target.
 * 
 * @author  Freeman Wang.
 * @version 2023-06-17
 */
public class Rocket extends Projectile
{
    private int rotationRate; // How much change in rotation per rotation update.
    private Actor target; // The target to, well, target.
    private GifImage rocketImage; // The gif of the rocket.
    private int homingDelay; // The delay between rotation updates, in milliseconds.
    private SimpleTimer homingTimer; // The timer for managing rotation updates.
    // Rockets leave a smoke trail, so these varibles control the trail properties.
    private int trailSize; // Length and width of the trail Particle.
    private int trailLifespan; // How long the trail Particle is visible, in milliseconds.
    private int trailDelay; // The millisecond delay between leaving a Particle.
    /**
     * Creates a rocket with 10 speed, 100 damage, 5 accuracy, 5 degree rotation rate, 100 ms homing delay.
     */
    public Rocket() {
        this(10, 100, 5, 5, 100);
    }
    /**
     * Creates a rocket that has a 5 degree rotation rate, 100ms homing delay.
     * 
     * @param speed     The speed of the rocket.
     * @param damage    How much damage the Rocket deals.
     * @param accuracy  How accurate the rocket is at initial launch. Higher is less accurate, 0 is most accurate. A bit of an useless variable though.
     */
    public Rocket(int speed, int damage, int accuracy) {
        this(speed, damage, accuracy, 5, 100);
    }
    /**
     * Creates a rocket.
     * 
     * @param speed     The speed of the rocket.
     * @param damage    The damage the rocket deals.
     * @param accuracy  The accuracy of the rocket. 0 is most accurate, higher is less accurate.
     * @param rotationRate  Maximum rotation change every rotation update.
     * @param homingDelay   The delay betwen rotation updates, in milliseconds.
     */
    public Rocket(int speed, int damage, int accuracy, int rotationRate, int homingDelay) {
        super(speed, damage, accuracy);
        this.rotationRate = rotationRate;
        this.homingDelay = homingDelay;
        this.trailSize = getImage().getHeight();
        this.trailLifespan = 1000;
        this.trailDelay = 25;
        
        collisionPoints = new Actor[4];
        homingTimer = new SimpleTimer();
        trailTimer = new SimpleTimer();
        rocketImage = new GifImage("Rocket_v2.gif");
        for (GreenfootImage image : rocketImage.getImages()) {
            image.scale(image.getWidth()*2, image.getHeight()*2);
        }
        setImage(rocketImage.getCurrentImage());
    }
    public void addedToWorld(World world) {
        setRotation(90);
        GameWorld game = (GameWorld) world;
        target = game.getPlayer();
        target(target);
    }
    public void act()
    {
        setImage(rocketImage.getCurrentImage());
        move(speed);
        target(target, rotationRate, homingDelay);
        if (isAtEdge()) {
            getWorld().removeObject(this);
            return;
        }
        else if (collisionCheck() != null) {
            Actor hitActor = collisionCheck();
            ((Player)hitActor).hit(damage);
            getWorld().removeObject(this);
            return;
        }
        leaveTrail(trailSize, trailLifespan, trailDelay);
    }
    // Checks top, bottom, left, right points if collides with player
    protected Actor collisionCheck() {
        collisionPoints[0] = getOneObjectAtOffset(-halfWidth, 0, Player.class); // Left
        collisionPoints[1] = getOneObjectAtOffset(0, -halfHeight, Player.class); // Top
        collisionPoints[2] = getOneObjectAtOffset(halfWidth, 0, Player.class); // Right
        collisionPoints[3] = getOneObjectAtOffset(0, halfHeight, Player.class); // Bottom
        for (Actor player : collisionPoints) {
            if (player != null) return player;
        }
        return null;
    }
    /**
     * Modified version of target() from the Projectile class. Does not directly turn towards the [target] but does it in
     * multiple rotation updates.
     * 
     * @param target        The Actor to target.
     * @param rotationRate  Maximum change of rotation every rotation update.
     * @param delay         The delay between rotation updates, in milliseconds.
     */
    private void target(Actor target, int rotationRate, int delay) {
        if (homingTimer.millisElapsed() >= delay && target.getWorld() != null) { // Time for a rotation update, and the target is existing,
            homingTimer.mark(); // Reset timer.
            int rotationCurrent = getRotation(); // save current rotation.
            turnTowards(target.getX(), target.getY());
            int requiredDegrees = getRotation(); // get required rotation.
            setRotation(rotationCurrent); // Set rotation back to current rotation.
            
            // I don't really know how I managed to do this... trash math kinda found at "homing missile math revised.png" in images folder.
            // But, it works. I don't know how to make this neater and don't have the time to, so I guess you get to see this...
            if (requiredDegrees > rotationCurrent) { 
                if ((requiredDegrees - rotationCurrent) > 180) {
                    // Turn CCW
                    if ((requiredDegrees - rotationCurrent) < rotationRate) turn(-(requiredDegrees-rotationCurrent)); // if required adjustment less than rotationRate turn that much
                    else turn(-rotationRate);
                }
                else {
                    // Turn CW
                    if ((requiredDegrees - rotationCurrent) < rotationRate) turn(requiredDegrees - rotationCurrent);
                    else turn(rotationRate);
                }
            }
            else if (rotationCurrent > requiredDegrees) {
                if ((rotationCurrent - requiredDegrees) > 180) {
                    // Turn CW
                    if ((rotationCurrent - requiredDegrees) < rotationRate) turn(rotationCurrent - requiredDegrees);
                    else turn(rotationRate);
                }
                else {
                    // Turn CCW
                    if ((rotationCurrent - requiredDegrees) < rotationRate) turn(-(rotationCurrent - requiredDegrees));
                    else turn(-rotationRate);
                }
            }
        }
    }
}

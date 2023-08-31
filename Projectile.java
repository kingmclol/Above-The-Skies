import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Projectile class. Projectiles are objects that can move around and
 * deal damage.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-17
 */
public abstract class Projectile extends Actor
{
    protected int speed; // The speed of the projectile.
    protected int damage; // The damage the projectile deals.
    protected int accuracy; // The maximum offset from the rotation the projectile is fired.
    protected int halfWidth, halfHeight; // Half width and height of the projectile's image.
    protected Actor[] collisionPoints; // Array of points to check for collision
    protected SimpleTimer trailTimer; // Timer for leaving a projectile trail.
    /**
     * Creates a projectile.
     * 
     * @param speed     The speed of the projecile.
     * @param damage    The amount of damage the projectile deals.
     * @param accuracy  The maximum degree offset from the initial rotation. An accuracy of 0 is 100% accurate.
     */
    public Projectile(int speed, int damage, int accuracy) {
        this.speed = speed;
        this.damage = damage;
        this.accuracy = accuracy;
        halfWidth = getImage().getWidth()/2;
        halfHeight = getImage().getHeight()/2;
        trailTimer = new SimpleTimer();
    }
    /**
     * Targets the [target] by making the projectile turn toward the [target]'s position.
     * If the [target] does not exist within the world, nothing happens.
     * 
     * @param target    The actor to target.
     */
    protected void target(Actor target) { 
        if (target.getWorld() != null) { // If target is in the world,
            turnTowards(target.getX(), target.getY()); // Turn towards the target's position.
        }
    }
    /**
     * Given the initial [rotation], return [rotation] + or - a random int from 0 to [offset].
     * 
     * @param rotation  The initial rotation.
     * @param offset    The maximum value to add or subtract from the rotation. Generally is the accuracy variable.
     */
    protected int addOffset(int rotation, int offset) {
        if (Greenfoot.getRandomNumber(2) == 0) { // 50% chance of this being true as Greenfoot.getRandomNumber(2) only returns 0 or 1.
            // Add a random int from 0 to offset.
            // Uses Greenfoot.getRandomNumber(offset+1) as getRandomNumber(int limit) only returns a int
            // from 0 (inclusive) to limit (exclusive). Adding 1 to the limit makes it so getRandomNumber
            // can possibly return limit.
            return rotation + Greenfoot.getRandomNumber(offset+1);
        }
        else  {
            // Subtract a random int fron 0 to offset.
            // See above for why offset+1 was used.
            return rotation - Greenfoot.getRandomNumber(offset+1);
        }
    }
    /**
     * Leaves a gray Particle trail, every [trailDelay] milliseconds.
     * 
     * @param trailSize     The length and width of the trail Particle.
     * @param trailLifespan How long the Particle hsould be visible for.
     * @param trailDelay    The interval between adding a Particle, in milliseconds.
     */
    protected void leaveTrail(int trailSize, int trailLifespan, int trailDelay) {
        leaveTrail(trailSize, trailLifespan, Color.GRAY, trailDelay);
    }
    /**
     * Leaves a Particle trail, every [trailDelay] milliseconds.
     * 
     * @param trailSize     The length and width of the trail Particle.
     * @param trailLifespan How long the Particle hsould be visible for.
     * @param trailColor    The colour of the Particle to leave.
     * @param trailDelay    The interval between adding a Particle, in milliseconds.
     */
    protected void leaveTrail(int trailSize, int trailLifespan, Color trailColor, int trailDelay) {
        if (trailTimer.millisElapsed() >= trailDelay) { // If the time elapsed is more than the trail delay,
            trailTimer.mark(); // Reset the timer, and
            getWorld().addObject(new Particle(trailSize, trailLifespan, trailColor), getX(), getY()); // Spawn the particle.
        }
    }
    /**
     * Abstract method for projectiles to check if they've hit an actor.
     * Each projectile should be checking various points and return the actor they have hit.
     */
    protected abstract Actor collisionCheck();
}

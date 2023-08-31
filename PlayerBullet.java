import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Bullets that the player fires. Checks if an Enemy was hit.
 * Removes itself from the world if at edge, or hit an Enemy.
 * 
 * @author  Freeman Wang
 * @version 2023-06-17
 */
public class PlayerBullet extends Bullet
{
    // Because the PlayerBullet has a trail, these variables specify the trail properties.
    private int trailSize; // The length and width of the trail Particles.
    private int trailLifespan; // How long the trail lasts, in milliseconds.
    private int trailDelay; // The millisecond delay between adding a trail Particle.
    /**
     * Creates a PlayerBullet with 100% accuracy and 5 damage.
     * 
     * @param speed     The speed of the PlayerBullet.
     */
    public PlayerBullet(int speed) {
        this(speed, 5, 0);
    }
    /**
     * Creates a PlayerBullet with 100% accuracy.
     * 
     * @param speed     The speed of the PlayerBullet.
     * @param damage    The damage the PlayerBullet deals.
     */
    public PlayerBullet(int speed, int damage) {
        this(speed, damage, 0);
    }
    /**
     * Creates a PlayerBullet with some inaccuracy.
     * 
     * @param speed     The speed of the PlayerBullet.
     * @param damage    The damage the PlayerBullet deals.
     * @param accuracy  Higher number is more inaccurate, 0 is 100% accurate.
     */
    public PlayerBullet(int speed, int damage, int accuracy) {
        super(speed, damage, accuracy);
        trailSize = getImage().getHeight(); // Trail size should be as big as the bullet.
        trailLifespan = 500;
        trailDelay = 5;
    }
    public void addedToWorld(World world) {
        int rotation = 270; // Upwards
        setRotation(addOffset(rotation, accuracy)); // Offset the rotation slightly based on accuracy.
    }
    public void act()
    {
        Actor hitEnemy = collisionCheck(); 
        if (isAtEdge()) { // IF at edge,
            getWorld().removeObject(this); // Remove this.
            return;
        }
        
        else if (hitEnemy != null) { // An enemy was hit.
            ((Enemy)hitEnemy).hit(damage); // Hit the enemy.
            getWorld().removeObject(this); // Remove this.
            return;
        }
        move(speed);
        leaveTrail(trailSize, trailLifespan, trailDelay); // Leave the trail Particle.
    }
    /**
     * Checks for 4 points: The top, bottom, left and right parts of the image.
     * 
     * @return  The Enemy that was hit. null if no enemy was hit.
     */
    protected Actor collisionCheck() {
        collisionPoints[0] = getOneObjectAtOffset(-halfWidth, 0, Enemy.class); // Left
        collisionPoints[1] = getOneObjectAtOffset(0, -halfHeight, Enemy.class); // Top
        collisionPoints[2] = getOneObjectAtOffset(halfWidth, 0, Enemy.class); // Right
        collisionPoints[3] = getOneObjectAtOffset(0, halfHeight, Enemy.class); // Bottom
        for (Actor enemy : collisionPoints) { // For each checked point,
            if (enemy != null) return enemy; // If there was an enemy hit, return it.
        }
        return null; // Else nothing was hit, so return nothing.
    }
}

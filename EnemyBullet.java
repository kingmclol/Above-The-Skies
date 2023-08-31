import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * EnemyBullets target the player and check if they hit the player. Removes at edge of world or hit hte player.
 * 
 * @author  Freeman Wang
 * @version 2023-06-17
 */
public class EnemyBullet extends Bullet
{
    /**
     * Creates a normal enemy bullet that deals 5 damage and is 100% accurate.
     * 
     * @param speed     The speed of the EnemyBullet.
     */
    public EnemyBullet(int speed) {
        super(speed, 5, 0);
    }
    /**
     * Creates an EnemyBullet with 100% accuracy.
     * 
     * @param speed     The speed of the EnemyBullet.
     * @param damage    How much damage the EnemyBullet deals.
     */
    public EnemyBullet(int speed, int damage) {
        super(speed, damage, 0);
    }
    /**
     * Creates an EnemyBullet with some inaccuracy.
     * 
     * @param speed     The speed of the EnemyBullet.
     * @param damage    How much damage the EnemyBullet deals.
     * @param accuracy  Actually is the inaccuracy. Higher number is less accurate, 0 is 100% accurate.
     */
    public EnemyBullet(int speed, int damage, int accuracy) {
        super(speed, damage, accuracy);
    }
    public void addedToWorld(World world) {
        setRotation(90);
        GameWorld game = (GameWorld) world;
        target(game.getPlayer()); // Turn towards the player.
        setRotation(addOffset(getRotation(), accuracy)); // Offset the rotation slightly based on accuracy.
    }
    public void act()
    {
        move(speed);
        if (isAtEdge()) { // At edge,
            getWorld().removeObject(this); // Remove this object.
            return;
        }
        else if (collisionCheck() != null) { // Something was hit.
            Actor hitActor = collisionCheck(); // Get the hit player.
            ((Player)hitActor).hit(damage); // Hit the player.
            getWorld().removeObject(this); // Remove this object.
            return;
        }
    }
    /**
     * Checks for 4 points: The top, bottom, left and right parts of the spherical image.
     * 
     * @return  The actor that was hit. null if the wanted class was not hit.
     */
    protected Actor collisionCheck() {
        collisionPoints[0] = getOneObjectAtOffset(-halfWidth, 0, Player.class); // Left
        collisionPoints[1] = getOneObjectAtOffset(0, -halfHeight, Player.class); // Top
        collisionPoints[2] = getOneObjectAtOffset(halfWidth, 0, Player.class); // Right
        collisionPoints[3] = getOneObjectAtOffset(0, halfHeight, Player.class); // Bottom
        for (Actor player : collisionPoints) { // For each checked point,
            if (player != null) return player; // If there was a player hit, return the player.
        }
        return null; // Else, nothing was hit, return null.
    }
}

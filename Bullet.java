import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class for bullets. I don't think it has any use after I cleaned up some of my code, but
 * I'm not going to change it.
 * 
 * @author  Freeman Wang
 * @version 2023-06-17
 */
public abstract class Bullet extends Projectile
{
    /**
     * Creates a bullet.
     * 
     * @param speed     The speed of the bullet.
     * @param damage    The damage of the bullet.
     * @param accuracy  The maximum degree offset from the initial rotation. An accuracy of 0 is 100% accurate.
     */
    public Bullet(int speed, int damage, int accuracy) {
        super(speed, damage, accuracy);
        collisionPoints = new Actor[4]; // Bullets check 4 points.
    }    
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A upgrade for increasing the player's shooting accuracy and bullet speed.
 * 
 * @author  Freeman Wang
 * @version 2023-06-15
 */
public class Goggles extends Powerup
{
    /**
     * Sets the gifImage to the proper image, scales it x2, sets speed to 2.
     */
    public Goggles() {
        gifImage = new GifImage("Goggles_v1.gif");
        scaleGif(2);
        speed = 2;
    }
    /**
     * Powerups the actor/player by increasing the accuracy by 2, and bulletSpeed by 1.
     * 
     * @param actor The player.
     */
    protected void powerup(Actor actor) {
        Player player = (Player) actor;
        player.increaseAccuracy(2);
        player.increaseBulletSpeed(1);
    }
}

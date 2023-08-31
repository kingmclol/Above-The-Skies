import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A shield upgrade for the player, increasing the max shield.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-15
 */
public class ShieldUpgrade extends Powerup
{
    /**
     * Sets the gifImage to the proper image, scales it x2, sets speed to 2.
     */
    public ShieldUpgrade() {
        gifImage = new GifImage("Shield_v1.gif");
        speed = 2;
        scaleGif(2);
    }
    /**
     * Powerups the actor/player by increasing player's max shield by 50 units.
     * 
     * @param actor The player.
     */
    protected void powerup(Actor actor) {
        Player player = (Player) actor;
        player.increaseMaxShield(50);
    }
}

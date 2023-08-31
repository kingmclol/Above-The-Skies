import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An upgrade for the player, decreasing the roll cooldown.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-15
 */
public class Fuel extends Powerup
{
    /**
     * Sets the gifImage to the proper image, scales it x2, sets speed to 2.
     */
    public Fuel() {
        gifImage = new GifImage("Fuel_v1.gif");
        scaleGif(2);
        speed = 2;
    }
    /**
     * Powerups the actor/player by decreasing the roll cooldown by 200 ms.
     * 
     * @param actor The player.
     */
    protected void powerup(Actor actor) {
        Player player = (Player) actor;
        player.decreaseRollCooldown(250);
    }
}

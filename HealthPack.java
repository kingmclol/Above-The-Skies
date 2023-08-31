import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Heals the player.
 * 
 * @author  Freeman Wang
 * @version 2023-06-15
 */
public class HealthPack extends Powerup
{
    /**
     * Sets the gifImage to the proper image, scales it x2, sets speed to 2.
     */
    public HealthPack() {
        gifImage = new GifImage("Health_Pack_v1.gif");
        scaleGif(2);
        speed = 2;
    }
    /**
     * Powerups the actor/player by healing the player 100 HP.
     * 
     * @param actor The player.
     */
    protected void powerup(Actor actor) {
        Player player = (Player) actor;
        player.heal(100);
    }
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An RPM upgrade for the player, making the player shoot faster.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-15
 */
public class AmmoUpgrade extends Powerup
{
    /**
     * Sets the gifImage to the proper image, scales it x2, sets speed to 2.
     */
    public AmmoUpgrade() {
        gifImage = new GifImage("Upgrade_Ammo_v1.gif");
        scaleGif(2);
        speed = 2;
    }
    /**
     * Powerups the actor/player by increasing the RPM by 100.
     * 
     * @param actor The player.
     */
    protected void powerup(Actor actor) {
        Player player = (Player) actor;
        player.increaseRPM(100);
    }
}

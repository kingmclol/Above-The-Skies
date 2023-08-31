import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An damage increase for the player's bullets.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AmmoUpgradeDamage extends Powerup
{
    /**
     * Sets the gifImage to the proper image, scales it x2, sets speed to 2.
     */
    public AmmoUpgradeDamage() {
        gifImage = new GifImage("Upgrade_Ammo_Damage_v2.gif");
        scaleGif(2);
        speed = 2;
    }
    /**
     * Powerups the actor/player by increasing bullet damage by 2.
     * 
     * @param actor The player.
     */
    protected void powerup(Actor actor) {
        Player player = (Player) actor;
        player.increaseDamage(2);
    }
}

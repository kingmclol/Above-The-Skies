import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A healthbar, but cooler because it's for the player.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-16
 */
public class BetterHealthBar extends StatBar
{
    private GifImage gif; // The cool gif to use.
    private GreenfootImage frame; // The frame to display.
    private StatBar healthBar; // The StatBar tied to this. Wait, do I even use this?
    private double scale; // Scales the gif
    private boolean hideAtDeath; // Determines if the BetterHealthBAr should be hidden when the owner is dead.
    /**
     * Creates a BetterHealthBar which is just a StatBar but with a cool animation.
     * 
     * @param owner         The actor the BetterHealthBar is tied to.
     * @param scale         How much the gif should be scaled by.
     * @param maxHealth     The max health of the BetterHealthBar.
     * @param hideAtDeath   Determines if this should be hidden at owner's death.
     */
    public BetterHealthBar(Actor owner, double scale, int maxHealth, boolean hideAtDeath){
        super(owner, (int)Math.round(194*scale), (int)Math.round(4*scale), maxHealth); // Dimensions determined from the gif I'm using.
        this.scale = scale;
        this.hideAtDeath = hideAtDeath;
        gif = new GifImage("Health_Bar_v2.gif");
        for (GreenfootImage image : gif.getImages()) {
            image.scale(round(image.getWidth()*scale), round(image.getHeight()*scale)); // Scale each image in the gif by scale.
        }
        frame = new GreenfootImage(round(gif.getCurrentImage().getWidth()*scale), round(gif.getCurrentImage().getHeight()*scale));
        setImage(frame);
    }
    public void addedToWorld(World world) {
        update();
    }
    /**
     * Updates the BetterHealthBar's image.
     */
    public void update() {
        super.update(); // Calls StatBar's update, updating the value and check if owner died.
        frame.clear(); // Clears the frame
        frame.drawImage(barImage,round(3*scale), round(8*scale)); // Draws the healthBar onto the frame.
        frame.drawImage(gif.getCurrentImage(), 0, 0); // Draws the current image of the gif on the frame.
        setImage(frame);
    }
    public void act()
    {
        if (hideAtDeath && owner.getWorld() == null) {  // Owner doesn't exist anymore then remove this from world.
            getWorld().removeObject(this);
            return;
        }
        update();
    }
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A shield bar, which is literally a health bar but blue. And has a cool animation.
 * 
 * @author  Freeman Wang
 * @version 2023-06-16
 */
public class ShieldBar extends StatBar
{
    private GifImage gif; // The cool gif to use.
    private GreenfootImage frame; // The frame to display.
    private StatBar shieldBar; // The StatBar tied to this. Wait, do I even use this?
    private double scale; // Scales the gif
    private boolean hideAtDeath; // Determines if the BetterHealthBAr should be hidden when the owner is dead.
    /**
     * Creates a ShieldBar. Seems familiar...
     * 
     * @param owner         The actor the ShieldBar is tied to.
     * @param scale         How much the gif should be scaled by.
     * @param maxShield     The max shield of the ShieldBar.
     * @param hideAtDeath   Determines if this should be hidden at owner's death.
     */
    public ShieldBar(Actor owner, double scale, int maxShield, boolean hideAtDeath){
        super(owner, (int)Math.round(194*scale), (int)Math.round(4*scale), maxShield, Color.CYAN, Color.RED); // Dimensions determined from the gif I am using.
        this.scale = scale;
        this.hideAtDeath = hideAtDeath;
        gif = new GifImage("Shield_Bar_v3.gif");
        for (GreenfootImage image : gif.getImages()) {
            image.scale(round(image.getWidth()*scale), round(image.getHeight()*scale)); // Scaling the gif by scale.
        }
        frame = new GreenfootImage(round(gif.getCurrentImage().getWidth()*scale), round(gif.getCurrentImage().getHeight()*scale));
        setImage(frame);
    }
    public void addedToWorld(World world) {
        update();
    }
    /**
     * Updates the ShieldBar's image.
     */
    public void update() {
        super.update(); // Update the tied StatBar
        frame.clear();
        frame.drawImage(barImage,round(3*scale), round(8*scale)); // Draws current statBar.
        frame.drawImage(gif.getCurrentImage(), 0, 0); // Take current frame from gif and draws it.
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

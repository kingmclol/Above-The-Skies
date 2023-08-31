import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Generic Stat Bar.
 * 
 * @author  Freeman Wang
 * @version 2023-06-16
 */
public class StatBar extends Actor
{
    GreenfootImage barImage; // The image of the StatBar.
    Actor owner; // The owner of the StatBar.
    private int valueMax; // The max value of the StatBar.
    private int valueCurrent; // The current value of the StatBar.
    private Color filled, empty; // Filled and empty colours.
    private boolean follow; // Determines if the StatBar should follow its owner.
    private boolean hideAtMax; // Determines if the StatBar should be invisible at max.
    /**
     * Creates a StatBar with an owner, width, height, maximum value, green filled colour, red empty colour, no follow, no hide at max.
     * Starts with current value equal to max value.
     * 
     * @param owner     The owner the StatBar is tied to.
     * @param width     The width of the StatBar.
     * @param height    The height of the StatBar.
     * @param max       The maximum value of the StatBar.
     */
    public StatBar(Actor owner, int width, int height, int max) { 
        this(owner, width, height, max, Color.GREEN, Color.RED);
    }
    /**
     * Creates a StatBar with an owner, witdh, height, maximum value, custom filled colour, custom empty colour, no follow, no hide at max
     * Starts with current value equal to max value.
     * 
     * @param owner     The owner the StatBar is tied to.
     * @param width     The width of the StatBar.
     * @param height    The height of the StatBar.
     * @param max       The maximum value of the StatBar.
     * @param filled    The colour for filled portions of the StatBar.
     */
    public StatBar(Actor owner, int width, int height, int max, Color filled, Color empty) {
        this(owner, width, height, max, max, filled, empty, false, false);
    }
    /**
     * Creats a StatBar with an owner, width, height, maximum value, initial value, custom filled colour, custom empty colour, following owner, hide at max
     * 
     * @param owner     The owner the StatBar is tied to.
     * @param width     The width of the StatBar.
     * @param height    The height of the StatBar.
     * @param max       The maximum value of the StatBar.
     * @param initial   The initial value of the StatBar.
     * @param filled    The colour for filled portions of the StatBar.
     * @param follow    Determines if the StatBar should follow the actor w/ 30 pixel offset down.
     * @param hideAtMax Determines if the StatBar should be invisible when full.
     */
    public StatBar(Actor owner, int width, int height, int max, int initial, Color filled, Color empty, boolean follow, boolean hideAtMax) {
        barImage = new GreenfootImage(width, height);
        this.filled = filled;
        this.empty = empty;
        this.owner = owner;
        this.follow = follow;
        this.hideAtMax = hideAtMax;
        valueMax = max;
        valueCurrent = initial;
    }
    public void addedToWorld() {
        update();
    }
    public void act()
    {
        if (owner.getWorld() == null) {  // Owner doesn't exist anymore then remove this from world
            getWorld().removeObject(this);
            return;
        }
        update();
        if (hideAtMax && valueCurrent == valueMax) { // If full and should hide at max,
            getImage().setTransparency(0); // Make invisible.
        }
        else getImage().setTransparency(255); // Else, be visible.
        
        if (follow) {
            setLocation(owner.getX(), owner.getY()+30); // Set location 30 pixels below the owner.
        }
    }
    /**
     * Updates the barImage.
     */
    protected void update() {
        barImage.clear();
        barImage.setColor(empty); // Full empty
        barImage.fill();
        barImage.setColor(filled);
        // Draw in the filled portion which is (valueCurrent/valueMax) percent of the bar's width.
        barImage.fillRect(0, 0, round(((double)valueCurrent/valueMax)*barImage.getWidth()), barImage.getHeight()); 
        setImage(barImage);
    }
    /**
     * Returns if the the current StatBar value is equal to zero.
     * 
     * @return  true if the current value is 0.
     */
    public boolean atZero() {
        return valueCurrent == 0;
    }
    /**
     * Makes the StatBar's current value decrease by [loss]. Does not go less than 0.
     * 
     * @param loss  The value that is subtracted from the current value.
     */
    public void loseValue(int loss) {
        valueCurrent = Math.max(0, valueCurrent-loss);
    }
    /**
     * Makes the StatBar's current value increase by [add]. Does not go over valueMax.
     * 
     * @param add   The value added to the current value.
     */
    public void addValue(int add) {
        valueCurrent = Math.min(valueMax, valueCurrent+add);
    }
    /**
     * Sets the StatBar's current value equal to [value]. If [value] is less than 0,
     * the current value is set to 0. If the [value] is greater than the maximum
     * value, the value is set to the max value.
     * 
     * @param value The value the current value is set to.
     */
    public void setValue(int value) {
        if (value > valueMax) valueCurrent = valueMax;
        else if (value < 0) valueCurrent = 0;
        else valueCurrent = value;
    }
    /**
     * Sets the max value to [maxValue].
     */
    public void setMaxValue(int maxValue) {
        valueMax = maxValue;
        update();
    }
    /**
     * Rounding. Hey, wait a minute... didn't I make one of these before?
     * 
     * @param x The double to be rounded.
     * @return  The rounded integer.
     */
    protected int round(double x) {
        return (int)(x+0.5);
    }
}

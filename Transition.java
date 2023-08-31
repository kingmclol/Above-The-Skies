import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The transition class has two types: FADE_IN, and FADE_OUT.
 * A FADE_IN transition will start at 0 transparency and become more opaque until fully black.
 * A FADE_OUT transition will start at 255 tranparency and become more transparent until 
 * fully transparent. If the world is of type WorldWithMusic, call the current world's 
 * goToNextWorld() method, telling the world it's time to go to the next one.
 * 
 * 
 * @author  Freeman Wang 
 * @version 2023-06-16
 */
public class Transition extends Actor
{
    SimpleTimer timer; // The timer
    int lifespan; // How long the transition takes, in milliseconds.
    int increment; // How much transparency change per update.
    int msPerUpdate; // The milliseconds per transparency update.
    String type; // What type of transition to execute.
    GreenfootImage image; // The image
    /**
     * Creates a transition.
     * 
     * @param lifespan  How long the transition should last for, in milliseconds.
     * @param increment How much transparancy difference every update. If 0, defaults to 2.
     * @param type      The type of transition. Must be "FADE_IN" or "FADE_OUT".
     */
    public Transition(int lifespan, int increment, String type){
        this.lifespan = lifespan;
        this.increment = ((increment == 0)? 2 : increment); // If increment is 0, make it become 2.
        this.type = type;
        msPerUpdate = lifespan/(256/this.increment); // Divides the lifespan by how many times the transparency shuld be updated.
        timer = new SimpleTimer();
    }
    public void addedToWorld(World world) {
        image = new GreenfootImage(world.getWidth(), world.getHeight());
        image.setColor(Color.BLACK);
        image.fill();
        if (type.equals("FADE_IN")) image.setTransparency(0); // Fading in, so begin as transparent.
        if (type.equals("FADE_OUT")) image.setTransparency(255); // Fading out, so begin as fully black.
        setImage(image);
    }
    public void act()
    {
        decay(); // Modify transparncy.
        if (transitionComplete()) { // The transition is complete.
            if (type.equals("FADE_IN")) { // The fading in transtion is complete.
                if (getWorld() instanceof WorldWithMusic) { // If the world is of type WorldWithMusic (has the goToNextWorld method)
                    ((WorldWithMusic)getWorld()).goToNextWorld(); // Go to the next world
                }
                getWorld().removeObject(this); // Does this do anything? Maybe. I don't know, actually. But should run if the world is not of type WorldWithMusic.
                return;
            }
            else if (type.equals("FADE_OUT")) { // The fading out transition is complete.
                getWorld().removeObject(this); // Simply remove the transition.
                return;
            }
        }
    }
    /**
     * Modifies the Transition's transparency depending on its type.
     */
    private void decay() {
        if (timer.millisElapsed() >= msPerUpdate) { // If the time elapsed is more or equal to the milliseconds between updates,
            timer.mark(); // Reset timer.
            if (type.equals("FADE_OUT")) { // If the transition is fading out,
                image.setTransparency(Math.max(0, image.getTransparency()-increment)); // Decrease the transparency. Do not go below 0.
            }
            else if (type.equals("FADE_IN")) { // IF the transition is fading in,
                image.setTransparency(Math.min(255, image.getTransparency()+increment)); // Increase the transparency. Do not exceed 255.
            }
            setImage(image); // Set the image.
        }
    }
    /**
     * Determines if the transition is complete or not.
     * If the type is FADE_IN, returns true if transparency is 255.
     * If the type is FADE_OUT, returns true if transparency is 0.
     * 
     * @return  true if the transition is complete, else if not.
     */
    public boolean transitionComplete() {
        if (type.equals("FADE_IN") && image.getTransparency() >= 255) return true; // Fading in, and fully opaque.
        else if (type.equals("FADE_OUT") && image.getTransparency() <= 0) return true; // Fading out, and fully transparent.
        else return false; // Transition is not done.
    }
}

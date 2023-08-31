import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Creates a circular actor in the world which loses transparency over time, removing itself when fully transparent.
 * 
 * @author  Freeman Wang
 * @version 2023-06-13
 */
public class Particle extends Actor
{
    protected int size; // Length and width of the particle.
    protected int lifespan; // in milliseconds
    protected int msPerDecay; // Milliseconds per transparency decay.
    protected int increment; // Amount of transparency lost every decay.
    protected Color color; // The color of the particle.
    protected GreenfootImage particleImage; // The image of the particle.
    protected SimpleTimer timer; // Timer to manage the decays.
    /**
     * Creates a particle of [size] length and width, 600ms lifespan, 5 transparency increment Gray colour.
     * 
     * @param size  The length and Width of the particle.
     */
    public Particle(int size) {
        this(size, 600, 5, Color.GRAY);
    }
    /**
     * Creates a particle of [size] length and width, [lifespan] ms ifespan, 5 transparency increment, Gray color.
     * 
     * @param size      The length and width of the particle. 
     * @param lifespan  How long the particle will exist, in milliseconds.
     */
    public Particle(int size, int lifespan) {
        this(size, lifespan, 5, Color.GRAY);
    }
    /**
     * Creates a particle of [size] length and width, [lifespan] ms lifespan, [increment] transparency increment, Gray colour.
     * 
     * @param size      The length and width of the particle.
     * @param lifespan  How long the particle will exist, in milliseconds.
     * @param increment How much transparency lost per increment.
     */
    public Particle(int size, int lifespan, int increment) {
        this(size, lifespan, increment, Color.GRAY);
    }
    /**
     * Creates a particle of [size] length and width, [lifespan] ms lifespan, 5 transparency increment, [color] color.
     *
     * @param size      The length and width of the particle.
     * @param lifespan  How long the particle will exist, in milliseconds.
     * @param color     The color of the particle. 
     */
    public Particle(int size, int lifespan, Color color) {
        this(size, lifespan, 5, color);
    }
    /**
     * Creates a particle of [size] length and width, [lifespan] ms lifespan, [increment] transparency increment, [color] color.
     * 
     * @param size      The length and width of the particle.
     * @param lifespan  How long the particle will exist, in milliseconds.
     * @param increment How much transparency is lost per increment.
     * @param color     The color of the particle.
     */
    public Particle(int size, int lifespan, int increment, Color color) {
        this.size = size;
        this.lifespan = lifespan;
        this.color = color;
        this.increment = increment;
        msPerDecay = lifespan/(255/increment);
        particleImage = new GreenfootImage(size, size);
        timer = new SimpleTimer();
    }
    public void addedToWorld(World w){
        setRotation(Greenfoot.getRandomNumber(360));
        particleImage.setColor(color);
        particleImage.fillOval(0, 0, size, size);
        setImage(particleImage);
    }
    public void act() {
        decay();
        if (particleImage.getTransparency() <= 0) { // If fully transparent,
            getWorld().removeObject(this); // Remove the particle.
            return;
        }
    }
    /**
     * Decrease the transparency of the particle by [increment] units every [msPerDecay] milliseconds.
     */
    protected void decay() {
        if (timer.millisElapsed() >= msPerDecay) { // If it's time to decay the particle,
            timer.mark(); // Reset timer,
            particleImage.setTransparency(Math.max(0, particleImage.getTransparency()-increment)); // Decrease transparency,
            setImage(particleImage); // Set image.
        }
    }
}

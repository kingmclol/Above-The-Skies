import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Abstract cass for powerups with a gifImage as their image.
 * Moves down every frame, and once it touches a player it will powerup() the player.
 * 
 * @author  Freeman Wang
 * @version 2023-06-15
 */
public abstract class Powerup extends Actor
{
    protected int speed; // The speed of the power up.
    protected GifImage gifImage; // The gif of the the powerup.
    // One powerup sound shared for all powerups. Otherwise the sound overlays if lots are collected at the same time...
    protected static GreenfootSound powerupSound = new GreenfootSound("Powerup_Collected.mp3");
    /**
     * Moves the powerup down [speed] pixels, kind of like move() but downwards.
     * 
     * @param speed How many pixels to move down.
     */
    protected void moveDown(int speed)
    {
        setLocation(getX(), getY()+speed);
    }
    public void act() {
        moveDown(speed); // Move down.
        setImage(gifImage.getCurrentImage()); // Set the image to the gif's current image.
        
        // Collision detection.
        Actor actor = getOneIntersectingObject(Player.class);
        if (actor != null) { // Touched the player.
            powerupSound.stop(); // Stop the sound if it is currently playing.
            powerupSound.play(); // Play the sound.
            powerup(actor); // Calls the abstract powerup() method.
            if (getWorld() instanceof GameWorld) { // If, the world is a GameWorld,
                ((GameWorld)getWorld()).addScore(200); // The player gains some score for their troubles, and gives a reason to collect these when capped out.
            }
            getWorld().removeObject(this);
            return;
        }
        else if (isAtEdge()) {// Touched the edge of the world.
            getWorld().removeObject(this);
            return;
        }
    }
    /**
     * Scales every image in the gif by a multiplier of [scale].
     * 
     * @param scale How much to scale the powerup's gif by.
     */
    protected void scaleGif(int scale) {
        for (GreenfootImage image : gifImage.getImages()) { // For each image in the gif,
            image.scale(image.getWidth()*scale, image.getHeight()*scale); // Scale it by scale scale. Is there a better way to say something is a variable in comments?
        }
    }
    /**
     * Abstract method that should be implemented in a way that powerups the player by calling
     * the correct player instance method.
     */
    protected abstract void powerup(Actor actor);
}

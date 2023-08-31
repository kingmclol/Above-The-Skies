import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A cloud actor with a GIF as it's image. Randomized speed and scale. Moves down. Removes itself when at edge.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-13
 */
public class Cloud extends Actor
{
    GifImage cloudGif; // The chosen cloud gif
    int maxSpeed; // The upper limit of the cloud's speed
    int speed; // The cloud's actual speed.
    double maxScale; // The max scale of the cloud.
    public Cloud() {
        // Initialize the possible cloud gifs.
        
        cloudGif = chooseCloudGif(); // Get a random cloud gif.
        maxSpeed = 5;
        speed = Greenfoot.getRandomNumber(maxSpeed)+1; // Get a random speed from 1 to maxSpeed.
        maxScale = 3;
    }
    public void addedToWorld(World w){
        // Why did I do this weird extra stuff again? Not touching this though.
        double scale = Greenfoot.getRandomNumber((int)(maxScale*100))/100d + 1; // Get a random scale, min is 1.00, max is 1.00 + maxScale
        for (GreenfootImage image : cloudGif.getImages()) {
                image.scale((int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
        };
    }
    public void act()
    {
        setImage(cloudGif.getCurrentImage()); // Set to curent image in GIF
        setLocation(getX(), getY()+speed); // Move down.
        if (isAtEdge()) {
            getWorld().removeObject(this);
            return;
        }
    }
    /**
     * Chooses a random cloud gif from my set of cloud gifs.
     */
    private GifImage chooseCloudGif() {
        // Basically gives each cloud type the same weighting, (2 for each type)
        // as there are two Cloud_3 versions.
        switch(Greenfoot.getRandomNumber(6)) { 
            case 0:
            case 1:
                return new GifImage("Cloud_1_v1.gif");
            case 2:
            case 3:
                return new GifImage("Cloud_2_v1.gif");
            case 4:
                return new GifImage("Cloud_3_v1.gif"); // Looks terrible lol
            default: // handles case 5 but is default to avoid error
                return new GifImage("Cloud_3_v2.gif");
        }
    }
}

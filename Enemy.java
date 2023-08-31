import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Generic enemy. Moves back and forth, has some HP, shoots a bullet or a rocket or a laser.
 * When dies gives score.
 * 
 * @author  Freeman Wang
 * @version 2023-06-13
 */
public class Enemy extends Actor
{
    private final int LASER_CHARGE_DELAY = 2000; // The delay before shooting the laser.
    private StatBar healthBar; // The healthbar tied to this enemy.
    private int maxHealth; // Max health of the healthbar
    private int shootRate; // Affects how much the enemy shoots. Less = faster shooting.
    private int bulletSpeed; // How fast a generic bullet moves.
    private int bulletDamage; // The damage a generic bullet deals.
    private int bulletAccuracy; // The acuracy of the shots. 0 = most accurate, higher = less acurrate.
    private int direction; // The direction the enemy is facing. 1 = left-facing, -1 = right-facing.
    private int moveSpeed; // The move normal speed of this enemy.
    private int currentSpeed; // The current speed of the enemy.
    private boolean charging; // Whether a laser is charing.
    private boolean laserExist; // Whether the laser is being fired or not.
    private SimpleTimer chargeTimer; // The charge timer.
    private GreenfootImage[][] images; // The images that the enemy can have.
    private GreenfootImage normalImage, angryImage; // Normal and angry images.
    private GreenfootSound deathSound, rocketSound, shootSound; // Various sounds.
    public Enemy() {
        this(50, 100, 10, 10, 10, 3);
    }
    public Enemy (int maxHealth, int shootRate, int bulletSpeed, int bulletDamage, int bulletAccuracy, int maxSpeed) {
        this.maxHealth = maxHealth;
        this.shootRate = shootRate;
        this.bulletSpeed = bulletSpeed;
        this.bulletDamage = bulletDamage;
        this.bulletAccuracy = bulletAccuracy;
        this.moveSpeed = Greenfoot.getRandomNumber(maxSpeed)+1;
        this.currentSpeed = moveSpeed;
        this.charging = false;
        
        // The 0th image in each array is normal enemy, 1st image is angry enemny.
        // For example, images[0][1] will give a Right-facing angry enemy.
        images = new GreenfootImage[][] {
            new GreenfootImage[] { // Right-facing images.
                new GreenfootImage("Enemy_v2.png"),
                new GreenfootImage("Enemy_Angry_v1.png")
            },
            new GreenfootImage[] { // Left-facing images.
                new GreenfootImage("Enemy_v2.png"),
                new GreenfootImage("Enemy_Angry_v1.png")
            },
        };
        for (GreenfootImage image : images[1]) {
            image.mirrorHorizontally(); // Flip all images in images[1].
        }
        
        deathSound = new GreenfootSound("Death.mp3");
        shootSound = new GreenfootSound("Enemy_Shoot.wav");
        shootSound.setVolume(55);
        rocketSound = new GreenfootSound("Enemy_Shoot_Rocket.wav");
        rocketSound.setVolume(40);
        do { // Get random direction
            direction = Greenfoot.getRandomNumber(3)-1;
        } while (direction == 0); // If direction is 0 try for new number again.
        healthBar = new StatBar(this, 50, 10, maxHealth, maxHealth, Color.GREEN, Color.RED, true, true);
        chargeTimer = new SimpleTimer();
    }
    public void addedToWorld(World world) {
        setImage(images[directionToIndex()][0]); // Normal enemy, facing in curent direction.
        world.addObject(healthBar, getX(), getY()+30);
    }
    public void act()
    {
        if (healthBar.atZero()) { // Dead
            for (int i = 0; i < 10; i++) { // Spawns 10 blood particles at current position, not going beyond the bounds of the image.
                getWorld().addObject(
                        new Particle(10, 1000, Color.RED)
                        , getX()-getImage().getWidth()/2+Greenfoot.getRandomNumber(getImage().getWidth())
                        , getY()-getImage().getHeight()/2+Greenfoot.getRandomNumber(getImage().getHeight()));
            }
            deathSound.play();
            spawnRandomPowerup(); // Attempt to spawn random powerup
            ((GameWorld)getWorld()).addScore(scoreCalculation()); // Add score to world
            getWorld().removeObject(this);
            return;
        }
        
        betterMove();
        
        if (charging) chargeLaser();
        if (Greenfoot.getRandomNumber(shootRate) == 0) attack();
    }
    /**
     * Makes this lose [damage] hp.
     * 
     * @param damage    The amount of damage this enemy takes.
     */
    public void hit(int damage) {
        healthBar.loseValue(damage);
    } 
    /**
     * Makes the enemy do a random attack between a bullet, rocket, or laser. 
     */
    private void attack() {
        if (charging) return; // Already charging a laser, no attacking.
        int rng = Greenfoot.getRandomNumber(20);
        if (rng < 4) { // Rocket
            rocketSound.stop();
            rocketSound.play();
            getWorld().addObject(
                new Rocket(),
                getX(),
                getY()+20);
        }
        else if (rng == 4 && !laserExist) { // LASER, only if one does not exist.
            charging = true; // Begin charging
            chargeTimer.mark();
            setImage(images[directionToIndex()][1]); // Become angry
            currentSpeed = 0; // Stop moving
        }
        else { // Bullet
            shootSound.stop();
            shootSound.play();
            getWorld().addObject(
                new EnemyBullet(bulletSpeed, bulletDamage, bulletAccuracy)
                , getX()
                , getY()+20);
        }
    }
    /**
     * Spawns a random powerup at the current position.
     */
    private void spawnRandomPowerup() {
        int x = Greenfoot.getRandomNumber(50); // Each increment worth 2%
        switch (x) {
            case 0:
                getWorld().addObject(new AmmoUpgrade(), getX(), getY());
                break;
            case 1:
                getWorld().addObject(new Fuel(), getX(), getY());
                break;
            case 2:
                getWorld().addObject(new Goggles(), getX(), getY());
                break;
            case 3:
                getWorld().addObject(new HealthPack(), getX(), getY());
                break;
            case 4:
                getWorld().addObject(new AmmoUpgradeDamage(), getX(), getY());
                break;
            case 5:
                getWorld().addObject(new ShieldUpgrade(), getX(), getY());
                break;
        }
    }
    /**
     * Sets the image to normal image and makes the move speed normal again.
     */
    public void moveAgain() {
        laserExist = false; // The laser is gone.
        currentSpeed = moveSpeed; // Move again!
        setImage(images[directionToIndex()][0]); // Become normal image.
    }
    /**
     * Charges the laser.
     * If the chargeTimer.millisElapsed() is greater than the LASER_CHARGE_DELAY a laser is spawned at the current position.
     */
    private void chargeLaser() {
        if (chargeTimer.millisElapsed() >= LASER_CHARGE_DELAY) { // If the time elapsed since began charging is more than the delay,
            charging = false; // The laser is... lasering, so not charging anymore.
            getWorld().addObject(new Laser(this), getX(), getY()+getWorld().getHeight()/2); // Spawn a laser.
            laserExist = true; // The laser is active.
        }
    }
    /**
     * Calculates how much score this enemy is worth.
     * 
     * @return  The amount of score that this enemy is worth.
     */
    private int scoreCalculation() {
        return maxHealth+ bulletDamage*2 + moveSpeed*3 + shootRate/2;
    }
    /**
     * Makes the enemy move. If at beyond the word sets it in a valid position.
     * If the enemy is at the edge mirror the image & direction.
     */
    private void betterMove() {
        move(direction * currentSpeed);
        if (getX() < 0) {
            setLocation(0, getY());
        }
        else if (getX() > getWorld().getWidth()) {
            setLocation(getWorld().getWidth(), getY());
        }
        if (isAtEdge()) {
            direction = -direction;
            setImage(images[directionToIndex()][0]);
        }
    }
    /**
     * Returns an integer based on the direction.
     * 
     * @return  if facing right (direction == 1), returns 0. else, returns 1.
     */
    private int directionToIndex() {
        if (direction == 1) return 0;
        else return 1;
    }
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * <------------------------------[Above the Skies]----------------------------------->
 * Those below may live their lives ignorant of the events occurring around the world,
 *                  and sometimes, it’s better if it is left that way. 
 * 
 * But how would their peaceful lives stay peaceful if there were no one protecting them?
 * 
 *            Unsung heroes known as GUARDIANS defend the land, sea, and air.
 * 
 *                  “Fight for the people, and fly Above the Skies.”
 * <---------------------------------------------------------------------------------->
 * @author Freeman Wang
 * @version 1.0
 * @date 2023-06-17
 * 
 * INSTRUCTIONS:
 *      -You start at the Main Menu, press [ENTER] to start the game.
 *      -You are the green plane.
 *          -Move using WASD controls.
 *              -[W]: Move up.
 *              -[A]: Move left.
 *              -[S]: Move down.
 *              -[D]: Move right.
 *          -[SPACE] to shoot.
 *              -You can hold [SPACE] down.
 *              -You cannot shoot with the shield active.
 *          -[TAB or J] to shield.
 *              -Shields negate damage to your healthbar.
 *              -Regenerates over time, only if the keys are released.
 *          -[SHIFT] to "roll" or "dash".
 *              -Become immune for a short period.
 *              -Boosts movement abilities.
 *       -The main goal is to kill the enemies by shooting at them until they die.
 *          -However, enemies will not just wait for their death--they will defend themselves!
 *          -Dodge their bullets and weapons to survive!
 *       -Each kill gives some score. Aim for the highest you can get!
 *          -There are multiple endings, depending on the score and time survived.
 *          -Aim for the highest score and survival time you can.
 *      -DO NOT PLAY AROUND WITH THE WORLD SPEED OR PAUSE THE GAME, AS THAT MESSES UP THE TIMERS.
 * ---------------------------------------------------------------------------------------------
 * CREDITS:
 * Music promoted by https://www.chosic.com/free-music/all/
 * 
 * Timeless by Alex-Productions | https://onsound.eu/
 * Last Hero by Alex-Productions | https://onsound.eu/
 * There Was A Time by Scott Buckley | www.scottbuckley.com.au
 * Moonlight by Scott Buckley | www.scottbuckley.com.au
 * Charlotte by Damiano Baldoni | 
 * Chase by Alexander Nakarada | 
 * Duel by Makai Symphony | 
 * Hitman by Kevin MacLeod | https://incompetech.com/
 * My Dark Passenger by Darren Curtis | 
 * Wallpaper by Kevin MacLeod | https://incompetech.com/
 * 
 * Sounds from Zapsplat.com
 * ---------------------------------------------------------------------------------------------
 * FEATURES:
 *      -Score system
 *      -Health and Shield system
 *      -Enemy scaling stats with time
 *      -Multiple endings
 *      -A story and theme
 *      -Powerups/upgades
 *      -Different enemy attaks
 *      -Music and sound effects
 * ----------------------------------------------------------------------------------------------
 * ISSUE LOG:
 *      -[NOT FIXING] Some of the logic is NOT based on framerate, but actual TIME elapsed. 
 *          -Some features that may be affected:
 *               -Player:
 *                   -Rolling cooldown, length
 *                   -Shooting cooldown
 *               -Rocket:
 *                   -Delay before adding particle
 *                   -Delay before changing rotation/homing
 *               -Laser: 
 *                   -Lifespan
 *                   -Delay beore hitting again
 *               -Transition:
 *                   -Lifespan
 *               -Particle:
 *                   -Lifespan
 *               -Text:
 *                   -Displaying dialogue
 *               -Worlds:
 *                   -Spawning clouds
 *           -Essentially, if the game's FPS was changed to be very slow or you use the act button
 *            directly, you will be able to do things like fire bullets every frame because time is
 *            still passing, just not as much act cycles.
 *               -The intended act speed is the default one.
 *       -[NOT REALLY AN ISSUE] The BetterHealthBar and ShieldBar's position (X, Y) is really strange. 
 *           -The issue may be related to scaling the original GIF, making positioning the object
 *            in the world much more difficult.
 *       -[PROBABLY FIXED] After letting the music go on for too long an error occours. For example:
 *          Exception in thread "SoundStream:file:/C:/Users/[HIDDEN]/Desktop/Media/Greenfoot/Versions/0.8/Above_The_Skies/sounds/There_Was_A_Time.mp3" java.lang.ArrayIndexOutOfBoundsException: Index 580 out of bounds for length 580
 *        There is no path to trace the error, so I think it may be something to do with the file itself. A search says that it may be caused by the file having some
 *        metadata tags, so I re-exported them using Audacity and removed the tags. 
 *       -[MAYBE FIXED] Using greenfoot's act button sometimes messes up the music playing. 
 *       -[PROBABLY FIXED] Pausing the game while a laser is present will have the laser sound keep playing. Actors do not have started() or stopped(). 
 *       -[ALTERNATIVE SOLUTION - limit RPM.] If the player's roundsPerMinute (the shooting speed) is too high, the sound will not play properly. This is because
 *        the sound file has a moment of silence before the sound, and shooting quickly will reset the sound before it
 *        plays. There are similar issues with other sounds. 
 *       -[NO SOLUTION?] Accuracy doesn't really function properly--a bullet with rotation of 269, 270, or 271 move the same for some reason. 
 *          -Essentially, having a +/- 1 degree rotation does not change its trajectory, while a rotation of +/- 3(?) does.
 *       -[PROBABLY FIXED] Enemy images may face the wrong way. 
 *       -[MAYBE FIXED] Lasers sometimes exist but the enemy has moved away/disappeared. 
 *       -[FIXED] Powerups do not remove itself from the world at edge. Fixed.
 *       
 *--------------------------------------------------------------------------------------------------
 * The start world, intruducing the player to the game and showing some keybinds and info.
 * The player can play around with the examplePlayer to try out its functions.
 * 
 * @author  Freeman Wang 
 * @version 2023-06-18
 */
public class StartWorld extends WorldWithMusic
{
    private Player examplePlayer; // The player that the player can play around with. Does not get carried over to other worlds.
    private Label infoLabelMain; // Main info label at the center of the screen.
    private Label title; // The title.
    private Label infoLabel; // Small info label at the corner.
    private String[][] menu; // The info menus.
    private int currentMenu; // The current menu that should be active.
    private final String CONTROLS_INFO; // Controls information.
    private final String UPGRADES_INFO; // Upgrades information.
    private final String UPGRADES_INFO_AMMO; // AmmoUpgrade info.
    private final String UPGRADES_INFO_DAMAGE; // AmmoUpgradeDamage info.
    private final String UPGRADES_INFO_FUEL; // Fuel info.
    private final String UPGRADES_INFO_GOGGLES; // Goggles info.
    private final String UPGRADES_INFO_HEALTH; // HealthPack info.
    private final String UPGRADES_INFO_SHIELD; // ShieldUpgrade info.
    public StartWorld()
    {    
        super();
        // Creates the songList.
        songList = new GreenfootSound[]{
            new GreenfootSound("Moonlight.mp3"),
            new GreenfootSound("There_Was_A_Time.mp3")
        };
        for (GreenfootSound song : songList) { // For each song in the songList,
            song.setVolume(95); // Give all songs 95 volume
        }
        songIndex = Greenfoot.getRandomNumber(songList.length); // Get random song index.
        currentSong = songList[songIndex]; // SEt current song to the song index.
        cloudTimer = new SimpleTimer(); // Using clouds, so create a cloudTimer.
        CONTROLS_INFO = String.join("\n",
                        "Press [ENTER] to start!",
                        "-----------------------------",
                        "[WASD]: Movement",
                        "[SPACE]: Shoot",
                        "[SHIFT]: Dash",
                        "[TAB or J]: Shield");
        UPGRADES_INFO = String.join("\n", // String.join is kinda a way to do multi line strings. In this case, it puts a new line between each line/parameter.
                        "Choose an UPGRADE to view.",
                        "-----------------------------",
                        "[1]: Ammo Upgrade",
                        "[2]: Damage Upgrade",
                        "[3]: Fuel",
                        "[4]: Goggles",
                        "[5]: Health Pack",
                        "[6]: Shield Upgrade");
        UPGRADES_INFO_AMMO = String.join("\n",
                        "Ammo Upgrade",
                        "-----------------------------",
                        "Increases rounds per minute, RPM, by 100.",
                        "Your default RPM is 500.",
                        "RPM caps at 1000.");
        UPGRADES_INFO_DAMAGE = String.join("\n",
                        "Damage Upgrade",
                        "-----------------------------",
                        "Increases bullet damage by 2.",
                        "Your default damage is 10.",
                        "Damage caps at 20.");
        UPGRADES_INFO_FUEL = String.join("\n",
                        "Fuel",
                        "-----------------------------",
                        "Decreases roll cooldown by 100 ms.",
                        "Your default cooldown is 2000 ms.",
                        "Minimum cooldown is 1000 ms.");
        UPGRADES_INFO_GOGGLES = String.join("\n",
                        "Goggles",
                        "-----------------------------",
                        "Increases bullet speed by 1.",
                        "Increases bullet accuracy.",
                        "Your default bullet speed is 10.",
                        "Bullet speed caps at 15.",
                        "Not explaining accuracy.");
        UPGRADES_INFO_HEALTH = String.join("\n",
                        "Health Pack",
                        "-----------------------------",
                        "Not really an upgrade...",
                        "Heals the player for 100 HP.",
                        "Excess healing is ignored.");
        UPGRADES_INFO_SHIELD = String.join("\n",
                        "Shield Upgrade",
                        "-----------------------------",
                        "Increases max shield by 50.",
                        "Your default max shield is 250.",
                        "Max shield caps at 500.");
        menu = new String[][] { // The infoMenu, managing all the strings to display on the labels.
            new String[] { // The CONTROLs menu.
                CONTROLS_INFO, // index 0 is for the infoLabelMain.
                "Press [RIGHT] to open the UPGRADEs info." // index 1 is for the infoLabel.
            },
            new String[] { // The UPGRADEs menu.
                UPGRADES_INFO,
                "Press [LEFT] to open the CONTROLs info."
            },
            new String[] { // Specific UPGRADEs menu.
                "Press [LEFT or RIGHT] to return to the UPGRADEs info.", // This is at index 0 so the upgrade info matches up with the key.
                UPGRADES_INFO_AMMO, // Everything else are possible things to display on infoLabelMain.
                UPGRADES_INFO_DAMAGE,
                UPGRADES_INFO_FUEL,
                UPGRADES_INFO_GOGGLES,
                UPGRADES_INFO_HEALTH,
                UPGRADES_INFO_SHIELD,
            }
        };
        
        currentMenu = 0; // Start at controls menu.
        prepare();
    }
    public void prepare() {
        transition("FADE_OUT");
        
        // The title.
        title = new Label("ABOVE THE SKIES", 72);
        addObject(title, getWidth()/2, 50);
        
        // Main info label.
        infoLabelMain = new Label(menu[currentMenu][0], 32);
        addObject(infoLabelMain, getWidth()/2, 100+infoLabelMain.getImage().getHeight()/2);
        setSkyBackground();
        
        // Small info label at the corner.
        infoLabel = new Label(menu[currentMenu][1], 24);
        addObject(infoLabel, infoLabel.getImage().getWidth()/2+10, getHeight()-infoLabel.getImage().getHeight()+10);
        
        // Spawns the tutorial player.
        examplePlayer = new Player(); 
        addObject(examplePlayer, getWidth()/2, getHeight()-150);
    }
    public void act() {
        checkSongState();
        
        spawnCloud();
        
        manageInfoMenu();
        
        if (Greenfoot.isKeyDown("enter")) {
            transition(); // Go to next world.
        }
    }
    /**
     * Manages reading keys for the infoMenu.
     */
    private void manageInfoMenu() {
        String key = Greenfoot.getKey();
        if (key == null) return; // Nothing pressed
        if ("right".equals(key)) { // Right arrow key pressed.
            currentMenu = Math.min(menu.length-2, currentMenu+1); // Go to next menu. Does not go past 1, as 2 is formatted differently.
            updateMenu();
        }
        else if ("left".equals(key)) { // Left arrow key pressed.
            currentMenu = Math.max(0, currentMenu-1); // Go back a menu. Does not go below 0.
            updateMenu();
        }
        else if (currentMenu == 1 && "123456".contains(key)) { // If the current menu is the UPGRADE info menu and the pressed key is a valid choice,
            currentMenu = 2; // Enter the specific upgrade info menu,
            updateMenu(Integer.valueOf(key)); // Show the specific upgrade.
        }
    }
    /**
     * Updates the infoMenu, only for when currentMenu is 0 or 1.
     */
    private void updateMenu() {
        if (currentMenu != 2) { // CONTROL info and UPGRADE info only. Otherwise breaks as specific upgrade info is formatted differently.
            infoLabelMain.setValue(menu[currentMenu][0]); // Sets the main label to the current menu.
            infoLabelMain.setLocation(getWidth()/2, 100+infoLabelMain.getImage().getHeight()/2); // Reposition the label as the string is different.
            
            infoLabel.setValue(menu[currentMenu][1]); // Sets the navigation label thing to the current menu's.
            infoLabel.setLocation(infoLabel.getImage().getWidth()/2+10, getHeight()-infoLabel.getImage().getHeight()+10); // Reposition.
        }
    }
    /**
     * Updates the infoMenu, only for when currentMenu is 2.
     */
    private void updateMenu(int upgrade) {
        if (currentMenu == 2) {// Only for specific Upgrade info.
            infoLabelMain.setValue(menu[currentMenu][upgrade]); // Sets the main label to the specific upgrade info.
            infoLabelMain.setLocation(getWidth()/2, 100+infoLabelMain.getImage().getHeight()/2); // Reposition.
            
            infoLabel.setValue(menu[currentMenu][0]); // Sets the navigation label thing to the current menu's.
            infoLabel.setLocation(infoLabel.getImage().getWidth()/2+10, getHeight()-infoLabel.getImage().getHeight()+10); // Reposition.
            
            spawnUpgrade(upgrade); // Spawns the upgrade being viewed.
        }
    }
    /**
     * Spawns the chosen upgrade at the slightly above the center of the world based on [upgrade].
     * 
     * @param upgrade   The upgrade to spawn.
     */
    private void spawnUpgrade(int upgrade) {
        Actor powerup = new AmmoUpgrade(); // AmmoUpgrade at default, becomes the proper one later.
        switch(upgrade) { // Depending on the chosen upgrade (the pressed key), the powerup becomes the upgrade.
            case 1:
                powerup = new AmmoUpgrade();
                break;
            case 2:
                powerup = new AmmoUpgradeDamage();
                break;
            case 3:
                powerup = new Fuel();
                break;
            case 4:
                powerup = new Goggles();
                break;
            case 5:
                powerup = new HealthPack();
                break;
            case 6:
                powerup = new ShieldUpgrade();
                break;
        }
        addObject(powerup, getWidth()/2, getHeight()/2-200); // Add the powerup to the world.
    }
    /**
     * Stops the current playing song and goes to the IntroWorld.
     */
    protected void goToNextWorld() {
        currentSong.stop();
        Greenfoot.setWorld(new IntroWorld());
    }
}
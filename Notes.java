public class Notes {
    // For naming this class. Ignore this.
}
/**
 * MOVED TO START WORLD MULTILINE COMMENT
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
 * @version 0.95
 * @date 2023-06-18
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
 * KNOWN BUGS/ISSUES:
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
 */

/**
 * TODO:
 * Add turning animation for player --> REJECTED
 * Different enemy types --> REJECTED
 * Player ammo counter --> REJECTED
 * Buff menu and applications --> MODIFIED, SEMI COMPLETE
 * Score system --> COMPLETE
 * Global Dialogue whioch appears in center of screen --> GOOD ENOUGH
 * Lore/story for game --> COMPLETE
 * Different endings --> COMPLETE
 * Create bossbar for boss (may use custom sprite like shieldbar) --> REJECTED
 * Powerups? --> COMPLETE
 * EXPLOSION ANIMATION, DEATH ANI<MATION -->SEMI COMPLETE/REJECTED
 * World transitions. -->DONE
 * Instruction menu --> DONE
 * playerbullets popping rockets --> REJECTED
 * projectile inaccuracy -->DONE
 * Sound attribution for songs and sound effects --> COMPLETE
 * Make enemy abstract class --> REJECTED
 * Combat event --> DONE
 * Interlude event --> REJECTED
 * Chase event --> REJECTED
 * Boss event --> REJECTED
 */

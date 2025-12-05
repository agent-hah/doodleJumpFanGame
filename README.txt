=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: 16442980
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections - I used linked lists to represent my platforms, monsters, and shurikens (which I
  called bullets in code). The linked lists were the best choice because the creation of the list
  has order built into it. For my platforms and monsters, the ones at the end of the list were more down,
  while the ones near the beginning where high up (this was the opposite for my bullets linked list.
  Additionally, I exploited the linkedlist's peakLast and peekFirst methods to quickly view the front
  and the back of each list when I needed to delete something because it would be off the game region
  when the game was scrolling up. The platform linkedlist size is relatively constant, because of
  design choice, but the monster and shuriken lists dynamically grow and shrink as it depends on how
  many objects of those types are on the game court. Because of the dynamically-adjusting-size aspect
  for monsters and bullets and the quick look up offered through the Linked List, I believe that I
  have an appropriate use of the collections library. My TA told me that 2D arrays aren't optimal for
  this game, so I pivoted to collections so that it can be more dynamic with the game.

  2. File I/O - I used file I/O for saving the game state between gaming sessions. I implemented it
  via a "Save" button, which would write the game state to a text file through a BufferedWriter. This
  game state would persist within the file unless one of three things happened:
    1. The game was reset
    2. The player died
    3. The save was loaded
  Once one of these happened, the file would be overwritten with "empty," removing the state data
  (stopping someone from saving the game, failing, and retrying). Once the file was written and
  the game closed, the player could continue playing the game by launching it once more, and it would
  load the game, paused, exactly where you left off. I believe this fulfills the file I/O requirement
  because it is able to effectively persist the game state between sessions (if the player chooses
  to save and quit the game).

  3. Inheritance/Subtyping - I believe I fulfill the subtyping requirement because of the multiple subtypes I
  have of platforms and monsters. There is more than one subtyping relationship, meaning that everything
  is not just a subtype of game object. More importantly, I have platform and monster subtypes that
  significantly override the supertype functionality and make use of dynamic dispatch. I have moving
  platforms that overrides the move method of the platforms, creating a platform that moves back and forth
  across the entire width of the game region, and I have a monster that does the same. I also have
  a monster that dynamically chases after the player, adjusting its direction depending on the location of the
  player relative to itself (named the Saucer), which also exploits dynamic dispatch (the move method) within
  the GameRegion. A more unique subtype I created is the disappearing platform, which uses a new method
  tick to increment its internal state to change from blue, to yellow, to red, and finally to disappear. This subtype
  is significantly unique from the platform supertype. I believe that all of these subtypes and supertypes that I listed
  satisfy the Inheritance/Subtyping design concept.

  4. JUnit Testable Component - I have written test cases that check collision logic between all
  the main types, and I also ensure that when a player bounces on a platform, the player's velocity
  goes up, and its upward velocity differs depending on the object. In my game model, I implemented
  that only the lower pixels of the player can interact with platforms, so I wrote an edge case
  where the platform is too high, and where the platform is at the right spot. I also wrote test
  cases to ensure that bullets only interact with monsters, and that the monster's internal
  state is changed because of the bullet collision. I also have more than 10 test cases present. This
  is an appropriate use of the concept as the underlying collision logic is paramount for the game to
  function properly, as the main aspects of the game (jumping up platforms), is contingent on collisions,
  so ensuring that this internal model works correctly is very important.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  GameObj - the abstract, overarching class that all the internal game objects inherit from. This is the class that has
  move methods, the abstract interact method, the intersects and willIntersect methods, the internal state position,
  velocity, and acceleration, and more (like the draw method). It is the basic skeleton that defines what an object
  as to be in order to be a dynamic part of the overall game.

  Player - the actual character that you control. This is the doodle jump person, and is the main character
  that keeps on jumping up and down on platforms, moving left and right to avoid monsters, and shooting shurikens
  to kill monsters.

  Bullet - these are what the player shoots out when you press space. These bullets only have a meaningful interaction
  with monsters, lowering their health by one with each collision and then deleting itself. This is the player's only
  weapon of defense against the monsters.

  Platform - The abstract class that defines all the platforms in the game. These platforms are what the
  player interacts with to go upwards.
    RegularPlatform - a regular platform that doesn't move and provides a standard upward velocity to the player.
    BouncyPlatform - like the regular platform, but gives a bigger velocity upwards.
    MovingPlatform - It gives a upward velocity that's in between the regular and bouncy platform,
    and moves left and right across the game region.
    WeakPlatform - a platform that the player can't interact with. This platform doesn't push the player up,
    instead the player "falls through" the platform, possible to its doom. The platform updates from a weak platform
    to a broken one.
    DisappearingPlatform - a platform that ticks over time, becoming closer to disappearing as the game continues.
    This platform doesn't move, but transitions from a blue platform to a yellow platform to a red platform, eventually
    disappearing and being un-interactable with the player (because it gets deleted from the internal game state).

  Monster - the abstract class that defines all the monsters in the game. These monsters are what kills the player if the
  player collides with them. All the monsters require colliding with the player in order to kill the player.
    RegularMonster - a regular, stationary monster with 1 hp.
    MovingMonster - a monster that moves left and right across the entire game region. It has 2 hp.
    HomingMonster - a monster that dynamically adjusts its direction to pursue the player. This saucer moves
    toward the player in a constant x speed and constant y speed. It has 2 hp.

  SaveReader - a public class that implements static methods to allow the game to be created from the savefile. The
  saveReader methods, depending on the object to be created from the line, takes the string, takes out the inputs,
  and creates the respective object (depends on the method called).

  RandomNumberGenerator - this is used to create the random generation that the game has. This is what's used to
  determine what type of platform will spawn, what type of monster will spawn, what will the initial internal
  state of the DisappearingPlatform be. It is also used to create a random x position for all of these objects.

  GameRegion - this is where the game happens. The player and all the platforms, monsters, and bullets are stored
  and drawn here. This is the main internal state of the game where the random generation occurs, where objects
  interact with each other, and where the x and y coordinates of all the objects are tracked (and where objects are
  deleted if they are out of bounds).

  Instructions - a basic JPanel filled with text detailing how to play the game, what the game is, and special features
  within the game. Doesn't really do anything special.

  RunDoodleJump - this is the class that implements the Runnable interface. It creates the "Save", "Reset",
  "resume"/"Start"/"Continue", "Pause", and "Instructions" buttons that directly interact with the game. It also
  is where the separate instructions frame is created, which uses the Instructions JPanel. Overall, this is where
  everything comes together within a window.

  Game - the most toplevel class that contains the main method where the game runs from. Everything related to the
  game is abstracted away from this class, so it's a pretty basic class to implement. It's the access point to running
  the game.

  DoodleTest - it's the where the test cases that I implemented are run to ensure that the internal game state
  functions as intended and does its job properly.

  CompilationTest - I didn't write this class, but I can tell you that it ensures that my code has the required
  things to be tested and graded properly.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  The main stumbling block I had was trying to figure out how to create subtypes that were significantly
  differentiated from each other and satisfied the inheritance/subtyping design component. The other stumbling
  block was figuring out all the collisions and making sure the right objects had the right collisions with each
  other.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I feel like there is a good separation of functionality because the overarching types of objects
are abstracted away from the internal game state, and theoretically, the people wouldn't be able to
see the savefile. The savefile is created through an outside, static method, which increases
the abstraction. I feel like through my classes and inheritance I hid most of the nuts
and bolts of the code, and it would especially true if you consider all of the helper functions I made
in order to prevent repeating blocks of code. THe main thing I would refactor, given the chance
is the collisions, so that they can be actually pixel perfect collisions, so that I would give the
game a more polished feel.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  Shuriken, weak platform, monster, doodleJump player images - https://doodlejump.net/games/doodle-ninja/

  Disappearing platform and moving platform images - https://de-academic.com/dic.nsf/dewiki/2331805

  Regular platform image - https://opengameart.org/content/grassy-platform

  Bouncy platform image - https://png.pngtree.com/png-vector/20221025/ourmid/pngtree-game-ground-asset-seamless-level-png-image_6373714.png
    (its from pngtree)
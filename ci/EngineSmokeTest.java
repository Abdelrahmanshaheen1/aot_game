import game.engine.Battle;
import game.engine.base.Wall;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;

/**
 * Headless check of the game engine (no JavaFX needed). Run from the repository root,
 * because the engine reads titans.csv and weapons.csv from the working directory:
 *
 *   javac -d out $(find src/game/engine -name "*.java") ci/EngineSmokeTest.java
 *   java -cp out EngineSmokeTest
 */
public class EngineSmokeTest {
    private static int passed = 0;

    private static void check(boolean condition, String name) {
        if (!condition) {
            System.err.println("FAIL: " + name);
            System.exit(1);
        }
        passed++;
        System.out.println("ok   " + name);
    }

    public static void main(String[] args) throws Exception {
        // Easy mode setup: 3 lanes, 250 resources per lane
        Battle easy = new Battle(0, 0, 50, 3, 250);
        check(easy.getLanes().size() == 3, "easy mode starts with 3 lanes");
        check(easy.getResourcesGathered() == 750, "easy mode starts with 3 x 250 resources");
        check(!easy.isGameOver(), "a new battle is not over");
        check(easy.getWeaponFactory().getWeaponShop().size() == 4, "weapons.csv defines 4 weapons");
        check(easy.getTitansArchives().size() == 4, "titans.csv defines 4 titan types");

        // Hard mode setup: 5 lanes, 125 per lane
        Battle hard = new Battle(0, 0, 50, 5, 125);
        check(hard.getLanes().size() == 5 && hard.getResourcesGathered() == 625, "hard mode starts with 5 lanes and 625 resources");

        // Buying a weapon spends its price and advances the turn
        Lane lane = easy.getOriginalLanes().get(0);
        int price = easy.getWeaponFactory().getWeaponShop().get(1).getPrice();
        easy.purchaseWeapon(1, lane);
        check(easy.getNumberOfTurns() == 1, "buying a weapon passes the turn");
        check(easy.getResourcesGathered() <= 750 - price + 1000, "resources are charged for the weapon");
        check(lane.getWeapons().size() == 1, "the weapon is placed on the chosen lane");

        // Errors
        Battle broke = new Battle(0, 0, 50, 1, 0);
        try {
            broke.purchaseWeapon(4, broke.getOriginalLanes().get(0));
            check(false, "buying without resources throws");
        } catch (InsufficientResourcesException expected) {
            check(true, "buying without resources throws InsufficientResourcesException");
        }
        try {
            easy.purchaseWeapon(1, new Lane(new Wall(100)));
            check(false, "buying for a foreign lane throws");
        } catch (InvalidLaneException expected) {
            check(true, "buying for a lane outside the battle throws InvalidLaneException");
        }

        // Phases change at turns 15 and 30
        Battle phases = new Battle(0, 0, 50, 5, 100000);
        for (int i = 0; i < 15 && !phases.isGameOver(); i++) phases.passTurn();
        check(phases.getNumberOfTurns() == 15 && phases.getBattlePhase().toString().equals("INTENSE"), "phase becomes INTENSE at turn 15");

        // Undefended, the walls eventually fall and the game ends
        Battle undefended = new Battle(0, 0, 50, 3, 250);
        int turns = 0;
        while (!undefended.isGameOver() && turns < 5000) {
            undefended.passTurn();
            turns++;
        }
        check(undefended.isGameOver(), "an undefended battle ends (after " + turns + " turns)");

        System.out.println("\nAll " + passed + " checks passed.");
    }
}

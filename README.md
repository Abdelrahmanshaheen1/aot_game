# Attack on Titan — Lane Defense Game (Java + JavaFX)

A turn-based, lane-based tower-defense game themed on *Attack on Titan*, written in
Java with a JavaFX GUI. Titans march down the lanes toward your walls; you spend
resources on weapons to stop them before every lane is destroyed.

> University OOP course project. Engine, data loading and GUI are all in this repo.

## Gameplay

- The battlefield is split into **lanes**, each protected by a **wall** (10,000 HP).
- Each turn, titans spawn and move toward the wall, weapons fire, and titans attack
  the wall when they reach it.
- You buy **weapons** with resources and place them on a lane of your choice.
- The battle moves through three phases — **Early, Intense, Grumbling** — which change
  which titans spawn.
- **Score** is the number of titans killed. The game is over when all lanes are destroyed.
- Two difficulty modes:

  | Mode | Lanes | Starting resources per lane |
  |------|-------|-----------------------------|
  | Easy | 3     | 250                         |
  | Hard | 5     | 125                         |

### Titans and weapons

| Code | Titan |
|------|-------|
| 1 | Pure Titan |
| 2 | Abnormal Titan |
| 3 | Armored Titan |
| 4 | Colossal Titan |

| Code | Weapon class | In-game name | Price | Damage |
|------|--------------|--------------|------:|-------:|
| 1 | Piercing Cannon | Anti Titan Shell | 25 | 10 |
| 2 | Sniper Cannon | Long Range Spear | 25 | 35 |
| 3 | Volley Spread Cannon | Wall Spread Cannon | 100 | 5 (range 20-50) |
| 4 | Wall Trap | Proximity Trap | 75 | 100 |

All of these numbers live in `titans.csv` and `weapons.csv`, so the game can be rebalanced
without touching the code.

## Project structure

cat > publish_aot_game.sh << 'SCRIPT_EOF'
#!/usr/bin/env bash
# Publishes the Attack on Titan game to the (currently empty) repo
# Abdelrahmanshaheen1/aot_game.
#
# Usage:   ./publish_aot_game.sh "/path/to/Milestone2solutionGIU.zip"
# Requires: gh CLI installed and authenticated (gh auth login), git, unzip.

set -euo pipefail

ZIP="${1:-$HOME/Desktop/Milestone2solutionGIU.zip}"
REPO="Abdelrahmanshaheen1/aot_game"
DIR="aot_game"

# The Milestone*PrivateTests / PublicTests files look like GIU's own test suites.
# Leave this as "no" unless your course allows sharing them publicly.
INCLUDE_TESTS="no"

# ---- sanity checks ---------------------------------------------------------
for cmd in gh git unzip; do
  command -v "$cmd" >/dev/null 2>&1 || { echo "!! '$cmd' is not installed"; exit 1; }
done
gh auth status >/dev/null 2>&1 || { echo "!! Not logged in. Run: gh auth login"; exit 1; }
[ -f "$ZIP" ] || { echo "!! Zip not found: $ZIP"; echo "   Pass the path as the first argument."; exit 1; }
[ ! -e "$DIR" ] || { echo "!! Folder './$DIR' already exists here. Move or delete it first."; exit 1; }
gh repo view "$REPO" >/dev/null 2>&1 || { echo "!! Can't see $REPO on GitHub."; exit 1; }
if gh api "repos/$REPO/commits" >/dev/null 2>&1; then
  echo "!! $REPO already has commits. Refusing to overwrite it."
  exit 1
fi

# ---- unzip (without the heavy / machine-specific stuff) --------------------
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
unzip -q "$ZIP" -x "*/lib/*" "*/.metadata/*" "*/bin/*" "*.DS_Store" -d "$TMP"

SRC_DIR="$(find "$TMP" -maxdepth 2 -type d -name src | head -1)"
[ -n "$SRC_DIR" ] || { echo "!! No src/ folder found in the zip."; exit 1; }
SRC_ROOT="$(dirname "$SRC_DIR")"

mkdir "$DIR"
cp -R "$SRC_ROOT"/. "$DIR"/
cd "$DIR"

# ---- clean up --------------------------------------------------------------
rm -f .project .classpath
find . -name ".DS_Store" -delete
if [ "$INCLUDE_TESTS" != "yes" ]; then
  rm -rf src/game/tests
fi
printf '\n# Local JavaFX SDK / JDK copies and build output\nlib/\nout/\n' >> .gitignore

# ---- README ----------------------------------------------------------------
cat > README.md << 'EOF'
# Attack on Titan — Lane Defense Game (Java + JavaFX)

A turn-based, lane-based tower-defense game themed on *Attack on Titan*, written in
Java with a JavaFX GUI. Titans march down the lanes toward your walls; you spend
resources on weapons to stop them before every lane is destroyed.

> University OOP course project. Engine, data loading and GUI are all in this repo.

## Gameplay

- The battlefield is split into **lanes**, each protected by a **wall** (10,000 HP).
- Each turn, titans spawn and move toward the wall, weapons fire, and titans attack
  the wall when they reach it.
- You buy **weapons** with resources and place them on a lane of your choice.
- The battle moves through three phases — **Early, Intense, Grumbling** — which change
  which titans spawn.
- **Score** is the number of titans killed. The game is over when all lanes are destroyed.
- Two difficulty modes:

  | Mode | Lanes | Starting resources per lane |
  |------|-------|-----------------------------|
  | Easy | 3     | 250                         |
  | Hard | 5     | 125                         |

### Titans and weapons

| Titans          | Weapons               |
|-----------------|-----------------------|
| Pure Titan      | Piercing Cannon       |
| Abnormal Titan  | Sniper Cannon         |
| Armored Titan   | Volley Spread Cannon  |
| Colossal Titan  | Wall Trap             |

Their stats live in `titans.csv` and `weapons.csv`, so the game can be rebalanced
without touching the code.

## Project structure

~~~
src/game/
├── engine/          # game logic, no GUI dependencies
│   ├── Battle.java            # turn loop, phases, lane setup, weapon purchases
│   ├── base/                  # Wall
│   ├── lanes/                 # Lane (lanes are ordered by danger level)
│   ├── titans/                # Titan hierarchy + TitanRegistry
│   ├── weapons/               # Weapon hierarchy, WeaponRegistry, WeaponFactory
│   ├── interfaces/            # Attacker, Attackee, Mobil
│   ├── dataloader/            # CSV parsing
│   └── exceptions/            # InsufficientResources, InvalidLane, InvalidCSVFormat
└── gui/             # JavaFX front end (FXML scenes, CSS, images)
    ├── Main.java, Controller.java, Model.java, View.java
    └── resources/
~~~

## Running it

Requirements: JDK 17+ and a [JavaFX SDK](https://gluonhq.com/products/javafx/) (built with 22.x)
for your operating system.

The game reads `titans.csv` and `weapons.csv` from the current directory, so run it
**from the repo root**:

~~~bash
export PATH_TO_FX=/path/to/javafx-sdk/lib

# compile
mkdir -p out
javac --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml \
      -d out $(find src -name "*.java" -not -path "src/game/tests/*")

# copy FXML / CSS / images next to the compiled classes
rsync -a --exclude='*.java' src/ out/

# run
java --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml \
     -cp out game.gui.Main
~~~

Or import the folder into Eclipse / IntelliJ, add JavaFX to the project, and run `game.gui.Main`.

## Tests

`ci/EngineSmokeTest.java` checks the game engine headlessly (no JavaFX needed): easy and hard
mode setup, CSV loading, buying a weapon, the insufficient-resources and invalid-lane errors,
the phase change at turn 15, and that an undefended battle ends. Run it from the repo root:

~~~bash
javac -d out $(find src/game/engine -name "*.java") ci/EngineSmokeTest.java
java -cp out EngineSmokeTest
~~~

GitHub Actions runs it on every push. Writing it exposed a bug that is now fixed: when the last
lane was destroyed during a turn, `Battle.performTurn` tried to spawn titans into a lane that no
longer existed and threw a `NullPointerException`. The GUI itself is not covered by the tests.

## License

The code is released under the [MIT License](LICENSE). *Attack on Titan* and its characters belong
to their respective owners; this is a non-commercial course project, and the images in
`src/game/gui/resources/images` are not covered by the MIT license.

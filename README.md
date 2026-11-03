# Casino Mod — Minecraft Java 1.20.1 (Forge)

A server-side casino minigame opened by right-clicking the Casino Key.

## Included
- Custom `Casino Key` item (`/give @p casinomod:casino_key`).
- Right-click opens the casino menu.
- Slot 1 accepts the wager; slot 2 is output-only and holds the prize.
- Placing a wager starts one roll automatically.
- The wager is consumed on a loss. On a win, the reward appears in slot 2.
- Chance scales with both item category/value and stack size. For a full stack (64):
  - Logs/planks: 5%
  - Stone/cobblestone: 10%
  - Iron: 25%
  - Gold: 40%
  - Diamond: 60%
  - Netherite: 80%
  - Other items use their rarity or a low default chance.
- Chance is capped at 90%.

## Build the JAR

Requirements: Java 17 and Gradle 8.1 or newer.

From this project directory run:

```bash
gradle build
```

The built mod will be at:

```text
build/libs/casinomod-1.0.0.jar
```

Copy that JAR into the `mods` folder of a Minecraft Forge 1.20.1 installation. The same mod JAR should be installed on the server and clients when playing multiplayer.

## Test in a world with cheats enabled

```mcfunction
/give @p casinomod:casino_key
```

Hold the key and right-click. Put a wager in the left slot. If you win, take the prize from the right slot before placing another wager.

## Notes

This is a source project, not a precompiled JAR. Build it with the command above. The project uses Forge 1.20.1-47.2.0 and official 1.20.1 mappings.

# Advanced Hud

Advanced Hud is a client-side Minecraft utility designed to provide real-time, dynamic information about targeted blocks and entities. It features a lightweight and customizable interface suitable for a variety of gameplay environments.

## Features

- **Target Detection**: Instantly identifies targeted blocks and entities using efficient raycasting.
- **Block Identification**: Displays the block display name and internal registry ID.
- **Entity Statistics**: Shows entity names and a precision health bar for living entities.
- **Configurable Interface**:
    - Global toggle for HUD visibility.
    - Adjustable X and Y screen offsets.
    - Toggleable visibility for specific data fields (e.g., internal block IDs).
- **Optimization**: Engineered for minimal performance overhead and maximum compatibility.

## Installation

1. Ensure a compatible mod loader is installed for your current Minecraft version.
2. Download the version of Advanced Hud that corresponds to your loader and game version.
3. Place the downloaded file into the `mods` folder of your Minecraft installation.
4. Launch the game and verify the overlay is active.

## Configuration

Customization options are managed via the `advanced-hud.json` configuration file, located in the `config` directory of your Minecraft instance.

| Option | Default | Description |
| :--- | :--- | :--- |
| `enabled` | `true` | Enables or disables the HUD overlay globally. |
| `xOffset` | `0` | Adjusts the horizontal position of the HUD. |
| `yOffset` | `10` | Adjusts the vertical position of the HUD. |
| `showEntityHealth` | `true` | Toggles the visibility of the entity health bar. |
| `showBlockId` | `true` | Toggles the visibility of internal block IDs. |

---

## Project Links

- [Modrinth](https://modrinth.com/mod/advancedhud)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/advanced-hud)
- [Wiki](https://github.com/yigit-guven/Advanced-Hud/wiki)
- [Issue Tracker](https://github.com/yigit-guven/Advanced-Hud/issues)
- [Source Code](https://github.com/yigit-guven/Advanced-Hud)
- [Discord Server](https://discord.gg/aPk7Qs5d4H)

---

## Developer

Developed by [Yigit Guven](https://github.com/yigit-guven).

- [Personal Website](https://yigitguven.net)
- [Development Profile](https://github.com/yigit-guven)

## License

Licensed under the GPL-3.0 License. See the [LICENSE](https://github.com/yigit-guven/Advanced-Hud/blob/fabric-1.20.1/LICENSE) file for more information.
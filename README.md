# Advanced HUD

Advanced HUD is a client-side Minecraft utility designed to provide real-time, dynamic information about targeted blocks and entities. It features a lightweight and highly customizable interface that matches Minecraft's vanilla aesthetic perfectly.

## Features

### Block Information
- **Effective Tool**: Instantly see which tool you need (Pickaxe, Axe, etc.) to mine the block.
- **Breaking Progress**: A smooth, visual progress bar appears at the bottom of the HUD as you break a block.
- **Crop Growth**: Displays the exact growth percentage for all major crops.
- **Waterlogged Status**: Indicates if a block is waterlogged at a glance.
- **Beehive Occupancy**: Shows exactly how many bees are currently inside a beehive or bee nest.
- **Container Details**: Displays item count and total capacity for chests, shulker boxes, and more (e.g., "Items: 15/27").

### Entity Statistics
- **Horse Breeding Stats**: Displays precise movement speed (in blocks/second) and jump strength for horses and variants.
- **Villager Profession**: Identifies the profession and level of any villager you look at.
- **Combat Stats**: Shows the entity's health using a heart-themed bar and displays their armor value.
- **Ownership**: Displays the owner's name for tamed entities (Wolves, Cats, etc.).
- **Registry ID**: Optional display of technical registry IDs and mod sources.

### Visuals & UI
- **Vanilla Aesthetic**: Tooltips feature a familiar purple border and gradient background inspired by Minecraft's inventory.
- **Dynamic Layout**: The interface automatically adjusts its height based on the available information.
- **Performance**: Optimized to ensure zero impact on your framerate.

## Compatibility
- **Vanilla Servers**: 100% compatible. Connect to any server (Realms, Hypixel, private SMPs) without issues.
- **Client-Side Only**: Does NOT need to be installed on the server.
- **Modpacks**: Designed to work seamlessly alongside other Fabric mods.

## Installation

1. Ensure a compatible mod loader is installed for your current Minecraft version.
2. Download the version of Advanced HUD that corresponds to your loader and game version.
3. Place the downloaded file into the `mods` folder of your Minecraft installation.
4. Launch the game and enjoy the enhanced HUD!

## Configuration

Customization options are managed via the `advanced-hud.json` configuration file, located in the `config` directory.

| Option | Default | Description |
| :--- | :--- | :--- |
| `enabled` | `true` | Enables or disables the HUD overlay globally. |
| `xOffset` | `0` | Adjusts the horizontal position. |
| `yOffset` | `10` | Adjusts the vertical position. |
| `showModName` | `true` | Shows which mod a target belongs to. |
| `showEffectiveTool` | `true` | Shows the required tool for blocks. |
| `showCropGrowth` | `true` | Shows growth percentage for crops. |
| `showWaterlogged` | `true` | Shows if a block is waterlogged. |
| `showBreakingProgress` | `true` | Shows the block breaking bar. |
| `showBeeCount` | `true` | Shows how many bees are in a hive. |
| `showContainerInfo` | `true` | Shows item counts for containers. |
| `showEntityHealth` | `true` | Shows the health bar for entities. |
| `showEntityArmor` | `true` | Shows the armor value of entities. |
| `showEntityOwner` | `true` | Shows the owner of tamed mobs. |
| `showHorseStats` | `true` | Shows horse speed and jump height. |
| `showVillagerInfo` | `true` | Shows villager profession and level. |
| `showEntityId` | `false` | Shows technical registry IDs. |

---

## Project Links

- [Modrinth](https://modrinth.com/mod/advancedhud)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/advanced-hud)
- [Issue Tracker](https://github.com/yigit-guven/Advanced-Hud/issues)
- [Source Code](https://github.com/yigit-guven/Advanced-Hud)

---

## Developer

Developed by [Yigit Guven](https://github.com/yigit-guven).

## License

Licensed under the GPL-3.0 License. See the [LICENSE](LICENSE) file for more information.
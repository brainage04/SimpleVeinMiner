# About
This is a very simple Minecraft Fabric mod that adds vein mining to Minecraft.
This mod is required on both the server and client to work.

Hold the Vein Mine Key (set to Grave by default) while mining blocks. Upon breaking a block, up to 64 of its neighbours (horizontal/vertical/diagonal) will be broken alongside it.

This functionality is toggleable through the `enableVeinMining` gamerule.
It is enabled by default.

The `maxBlocks` gamerule determines how many blocks will be broken at most in one vein mine,
and the `maxVisitedBlocks` gamerule determines how many blocks will be checked for breaking at most in one vein mine

To modify these gamerules, either use the `/gamerule` command
or the `/simpleveinminer` command, which allows you to manage only the gamerules registered with this mod for ease of access purposes.
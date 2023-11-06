# SiegeCore
A minecraft spigot server plugin written in java/Maven. This plugin establishes basic interface for data type registration, item interaction, player data handling, serialization/deserialization of all data types above.

# Core data types:
AbstractItem, handles custom items with event listeners to handle left click, right click, right hold, swap click/hold, sprint toggle, sneak hold/toggle.
AbstractPlayer, binds with player and creates when a new player is online.
PFile, superclass of all configuration/data, handles serialization/deserialization using jackson.
PType, basic interface for other plugins to register a new subclass of AbstractItem/AbstractPlayer/PFile.

## PType:
PType can be registered using PType.putPType(...). Calling this method will register a new file type with path of plugins/SiegeXXX/XXX. All jsons in that path will be loaded.

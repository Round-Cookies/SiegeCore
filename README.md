# SiegeCore
A minecraft spigot server plugin written in java. It handles json serialization/deserialization using jackson and implements a basic item manager. Event handlers and wrappers are planned to be included in the near future.

# Core data types:
Two key data types are implemented to help java serialization/deserialization:

## PType:
PType can be registered using PType.putPType(...). Calling this method will register a new file type with path of plugins/SiegeXXX/XXX. All jsons in that path will be loaded.

## PFile:
PFile is the common superclass of all introduced data classes. It handles IO operations easily with good expandability.

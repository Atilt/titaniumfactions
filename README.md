# titaniumfactions
High performance Factions fork that prioritizes efficiency, speed, and low memory footprints.

## Features
- Reduced debug information
- Reduced memory usage
- Dropped name-to-UUID conversion for legacy Minecraft versions
- Dropped String based UUID implementation for actual UUIDs
- Faster query times for local and in-memory data
- Faster math implementations for particles, chunk claiming, and block scanning
- Faction, claim, and player data are loaded async
- Proper async implementations for local data saving
- Optimized land fetching & claiming
- Implements fast and low memory collections and maps

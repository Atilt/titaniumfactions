![](https://i.imgur.com/PCbrx7F.png "Banner")

# titaniumfactions
High performance Factions fork that prioritizes efficiency, speed, and low memory footprints.

## Optimizations [Improvements]
- Reduced debug information
- Reduced memory usage
- Faction, claim, and player data are loaded async
- Dropped name-to-UUID conversion for legacy Minecraft versions
- Dropped string based UUID implementation for actual UUIDs
- Dropped string based integer faction ids for actual integers
- Faster query/fetching times for local and in-memory data
- Faster math implementations for particles, chunk claiming, and block scanning
- Faster faction data serialization & deserialization
- Faster, less intensive land fetching and claiming
- Faster translation parsing for less-intensive mass message sending
- Optimized faction [player] inactivity checks
- Optimized faction flight checks
- Proper async implementations for local data saving
- Implements fast and low memory collections and maps
- Implements cache for faction locations
- Replaces fanciful api with kyori-text api

## Features (in progress)
- Wild teleportation
- Better world border mechanics (liquid flow, block gravity, etc.)
- Redstone liquid protection
- Reserve faction names
- Factions shields to prevent explosions in claimed territory for X duration

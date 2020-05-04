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
- Optimized faction [player] inactivity checks
- Proper async implementations for local data saving
- Implements fast and low memory collections and maps
- Implements cache for faction locations
- Replaces fanciful api with kyori-text api

## Features
- N/A

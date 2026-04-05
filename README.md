# Simply Airdrops

Lightweight Forge 1.20.1 mod that spawns configurable airdrop events and lets you call them with a radio controller. Drops contain modular loot crates (wooden airdrop box, medic crate, plus upcoming ammo/weapon cases) and everything is driven by JSON so you can tweak timers, loot, and behavior without touching code.

## Modname – Simply Airdrops
- Recurring airdrops trigger every configurable `X` ticks, but you can also pop a drop manually with the radio controller.
- Each airdrop delivers a wooden `Airdrop Box` and a `Medic Crate` today, with ammo boxes and weapon cases in the pipeline.
- Every crate wires into GeckoLib models for 3D hand rendering and keeps contents when broken; the loot inside is defined via the mod’s JSON loot tables.

## Features
- Scheduled drops with a configurable interval (default behavior is to keep the sky raining loot).
- Radio controller item to call in drops at will.
- Modular crate types backed by per-crate loot tables so you can change drop contents without rebuilding.
- JSON-first configuration covering drop timing, spawn windows, loot tables, and crate behavior.

## Implemented
- Wooden `Airdrop Box` (GEODE-based model, 3D hand view, persistent storage, accurate hitbox).
- `Medic Crate` (9-slot storage, GeckoLib animations, custom GUI offset).

## Planned
- Ammo Box and Weapon Case crates.
- Finish wiring the rest of the planned feature list (more crate types, refined loot controls, extra triggers).

## Compatibility
- Mod loader: Forge
- Minecraft version: 1.20.1

## Disclaimer
This project is an independent fan-made creation and has no affiliation with Mojang Studios or Minecraft. Please do not report bugs or issues to Mojang or Minecraft support—send any feedback, bug reports, or questions directly to the developer.

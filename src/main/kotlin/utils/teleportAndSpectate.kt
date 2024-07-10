package com.github.trard.utils

import com.github.trard.Scheduler
import org.bukkit.GameMode
import org.bukkit.entity.Player

fun teleportAndSpectate(scheduler: Scheduler, spectator: Player, target: Player) {
    spectator.gameMode = GameMode.SPECTATOR

    scheduler.runAtEntityLater(target, 10, null) {
        spectator.teleportAsync(target.location)
        spectator.gameMode = GameMode.SPECTATOR
        spectator.spectatorTarget = null
    }

    // elytra speed at 52 degrees is ~3.365 blocks/tick (https://minecraft.fandom.com/wiki/Transportation 67.3 m/s)
    // average minecraft view distance is 8 chunks so 8*16=128 blocks
    // round(128 / 3.365) = 38 ticks enough to get out from view distance
    // 37 because 1 tick for assurance
    //
    // it's possible to break this limit faster, but it's probably never going to happen
    scheduler.runAtEntityLater(target, 37, null) {
        spectator.teleportAsync(target.location)
        spectator.gameMode = GameMode.SPECTATOR
        spectator.spectatorTarget = target
    }
}
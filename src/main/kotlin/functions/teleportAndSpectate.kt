package com.github.trard.functions

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

    scheduler.runAtEntityLater(target, 37, null) {
        spectator.teleportAsync(target.location)
        spectator.gameMode = GameMode.SPECTATOR
        spectator.spectatorTarget = target
    }
}
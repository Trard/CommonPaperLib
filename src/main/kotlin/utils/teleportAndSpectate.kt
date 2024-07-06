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

    scheduler.runAtEntityLater(target, 60, null) {
        spectator.teleportAsync(target.location)
        spectator.gameMode = GameMode.SPECTATOR
        spectator.spectatorTarget = target
    }
}
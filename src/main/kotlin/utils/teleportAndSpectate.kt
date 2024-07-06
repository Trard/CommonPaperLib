package com.github.trard.utils

import com.github.trard.Scheduler
import org.bukkit.entity.Player

fun teleportAndSpectate(scheduler: Scheduler, spectator: Player, target: Player) {
    scheduler.runAtEntityLater(target, 10, null) {
        spectator.teleportAsync(target.location)
        spectator.spectatorTarget = null
    }

    scheduler.runAtEntityLater(target, 60, null) {
        spectator.teleportAsync(target.location)
        spectator.spectatorTarget = target
    }
}
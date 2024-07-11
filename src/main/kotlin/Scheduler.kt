package com.github.trard

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin


class Scheduler(private val plugin: Plugin) {
    private val isFolia = run {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    fun runInRegion(location: Location, runnable: Runnable) {
        if (isFolia) Bukkit.getRegionScheduler().run(plugin, location) { runnable.run() }
        else Task.BukkitTask(Bukkit.getScheduler().runTask(plugin, runnable))
    }

    fun runInRegionLater(location: Location, delayTicks: Long, runnable: Runnable) {
        if (isFolia) Bukkit.getRegionScheduler().runDelayed(plugin, location, { runnable.run() }, delayTicks )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks))
    }

    fun runInRegionAtFixedRate(location: Location, delayTicks: Long, period: Long, runnable: Runnable): Task {
        return if (isFolia) Task.FoliaTask(
            Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, { runnable.run() }, delayTicks, period)
        )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, period))
    }

    fun runGlobalAtFixedRate(delayTicks: Long, period: Long, runnable: Runnable): Task {
        return if (isFolia) Task.FoliaTask(
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, { runnable.run() }, delayTicks, period)
        )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, period))
    }

    fun runGlobalLater(delayTicks: Long, runnable: Runnable) {
        if (isFolia) Bukkit.getGlobalRegionScheduler().runDelayed(plugin, { runnable.run() }, delayTicks )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks))
    }

    fun runAtEntityLater(entity: Entity, delayTicks: Long, retired: Runnable?, runnable: Runnable) {
        if (isFolia) entity.scheduler.runDelayed(plugin, { runnable.run() }, retired, delayTicks )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks))
    }

    fun runAtEntityAtFixedRate(entity: Entity, delayTicks: Long, periodTicks: Long, retired: Runnable?, runnable: Runnable) {
        if (isFolia) entity.scheduler.runAtFixedRate(plugin, { runnable.run() }, retired, delayTicks, periodTicks )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks))
    }

    sealed class Task {
        class FoliaTask(val task: ScheduledTask) : Task()
        class BukkitTask(val task: org.bukkit.scheduler.BukkitTask) : Task()

        fun cancel() {
            when (this) {
                is FoliaTask -> task.cancel()
                is BukkitTask -> task.cancel()
            }
        }
    }
}
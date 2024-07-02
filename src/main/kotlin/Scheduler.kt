import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin


class Scheduler(private val plugin: Plugin) {
    private val isFolia: Boolean = Bukkit.getVersion().contains("Folia")

    fun runInRegion(location: Location, runnable: Runnable) {
        if (isFolia) Bukkit.getRegionScheduler().run(plugin, location) { runnable.run() }
        else Task.BukkitTask(Bukkit.getScheduler().runTask(plugin, runnable))
    }

    fun runInRegionLater(location: Location, delayTicks: Long, runnable: Runnable) {
        if (isFolia) Bukkit.getRegionScheduler().runDelayed(plugin, location, { runnable.run() }, delayTicks )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks))
    }

    fun runInRegionAtFixedRate(location: Location, delay: Long, period: Long, runnable: Runnable): Task {
        return if (isFolia) Task.FoliaTask(
            Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, { runnable.run() }, delay, period)
        )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period))
    }

    fun runGlobalAtFixedRate(delay: Long, period: Long, runnable: Runnable): Task {
        return if (isFolia) Task.FoliaTask(
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, { runnable.run() }, delay, period)
        )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period))
    }

    fun runGlobalLater(location: Location, delayTicks: Long, runnable: Runnable) {
        if (isFolia) Bukkit.getGlobalRegionScheduler().runDelayed(plugin, { runnable.run() }, delayTicks )
        else Task.BukkitTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks))
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
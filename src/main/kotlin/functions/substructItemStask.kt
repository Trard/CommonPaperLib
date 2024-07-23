package com.github.trard.functions

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

fun subtractItemStack(inventory: PlayerInventory, itemStack: ItemStack) {
    var remaining = itemStack.amount

    for (item in inventory) {
        if (item != null && item.type == itemStack.type) {
            if (remaining-item.amount > 0) {
                remaining -= item.amount
                item.subtract(item.amount)
            } else {
                item.subtract(remaining)
                return
            }
        }
    }
}
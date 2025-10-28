package app.lyrablock.lyra.feature.mining


/**
 * @param abilityName The display name (what Hypixel gives you).
 * @param cooldownList The ability cooldown in seconds, corresponding to 3 levels.
 * @param activeTimeList The active time in seconds, corresponding to 3 levels.
 */

enum class PickaxeAbility(val abilityName: String, val cooldownList: IntArray, val activeTimeList: IntArray) {
    MINING_SPEED_BOOST("Mining Speed Boost", intArrayOf(120, 120, 120), intArrayOf(10, 15, 20)),
    PICKOBULUS("Pickobulus", intArrayOf(60, 50, 40), intArrayOf(0, 0, 0)),
    TUNNEL_VISION("Tunnel Vision", intArrayOf(120, 110, 100), intArrayOf(30, 30, 30)),
    MANIAC_MINER("Maniac Miner", intArrayOf(60, 60, 60), intArrayOf(25, 30, 35)),
    GEMSTONE_INFUSION("Gemstone Infusion", intArrayOf(120, 120, 120), intArrayOf(20, 25, 30)),
    SHEER_FORCE("Sheer Force", intArrayOf(120, 120, 120), intArrayOf(20, 25, 30));

    init {
        assert(this.cooldownList.size == 3 && this.activeTimeList.size == 3)
    }
}

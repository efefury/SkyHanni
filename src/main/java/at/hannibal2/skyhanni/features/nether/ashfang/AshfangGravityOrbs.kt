package at.hannibal2.skyhanni.features.nether.ashfang

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.test.GriffinUtils.drawWaypointFilled
import at.hannibal2.skyhanni.utils.ItemUtils.getSkullTexture
import at.hannibal2.skyhanni.utils.LocationUtils
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.SpecialColour
import at.hannibal2.skyhanni.utils.getLorenzVec
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

class AshfangGravityOrbs {

    private val texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV" +
            "0L3RleHR1cmUvMWE2OWNjZjdhZDkwNGM5YTg1MmVhMmZmM2Y1YjRlMjNhZGViZjcyZWQxMmQ1ZjI0Yjc4Y2UyZDQ0YjRhMiJ9fX0="
    private val orbs = mutableListOf<EntityArmorStand>()

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!isEnabled()) return

        Minecraft.getMinecraft().theWorld.loadedEntityList
            .filter { it ->
                it is EntityArmorStand && it !in orbs && it.inventory
                    .any { it != null && it.getSkullTexture() == texture }
            }.forEach { orbs.add(it as EntityArmorStand) }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!isEnabled()) return

        val special = SkyHanniMod.feature.abilities.ashfangGravityOrbsColor

        val color = Color(SpecialColour.specialToChromaRGB(special), true)

        val playerEyeLocation = LocationUtils.playerEyeLocation()
        for (orb in orbs) {
            if (orb.isDead) continue
            val orbLocation = orb.getLorenzVec()
            if (!LocationUtils.canSee(playerEyeLocation, orbLocation.add(0.0, 2.0, 0.0))) continue

            event.drawWaypointFilled(orbLocation.add(-0.5, 1.25, -0.5), color)
        }
    }

    @SubscribeEvent
    fun renderOverlay(event: WorldEvent.Load) {
        orbs.clear()
    }

    private fun isEnabled(): Boolean {
        return LorenzUtils.inSkyblock && SkyHanniMod.feature.abilities.ashfangGravityOrbs
    }
}
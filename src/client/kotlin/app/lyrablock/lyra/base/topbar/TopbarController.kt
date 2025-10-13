package app.lyrablock.lyra.base.topbar

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.util.LyraIdentifier
import app.lyrablock.orion.RenderRoot
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements

@LyraModule
object TopbarController {
    init {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, LyraIdentifier.of("topbartest")) { context, _ ->
            RenderRoot(TopbarWidget()).render(context, 100, 10)
        }
    }
//    const val PADDING_X = 8
//    const val PADDING_Y = 4
//    var width: Double by Animated(0.0, 200.milliseconds, Double.interpolator, TimeFunction.EXP_EASE_OUT)
//    var height: Double by Animated(0.0, 200.milliseconds, Double.interpolator, TimeFunction.EXP_EASE_OUT)
//    var opacity: Double by Animated(0.0, 200.milliseconds, Double.interpolator)
//    var stickyIndex: Int by observable(-2) { property, oldValue, newValue ->
//        if (newValue == -1) {
//            width = 0.0
//            opacity = 0.0
//            return@observable
//        }
//    }
//
//    init {
//        HudLayerRegistrationCallback.EVENT.register {
//            it.attachLayerAfter(
//                IdentifiedLayer.MISC_OVERLAYS, LyraIdentifier.of("top_bar"), ::render
//            )
//        }
//    }
//
//    val stickyCandidates = mutableListOf<BarCandidate>()
//
//    fun addSticky(priority: Int = 0, callback: () -> LyraDrawable?) {
//        stickyCandidates.add(object : BarCandidate() {
//            override val priority = priority
//            override fun getDrawable() = callback()
//        })
//        stickyCandidates.sortByDescending { it.priority }
//    }
//
//    private fun select() {
//        stickyIndex = stickyCandidates.indexOfFirst { it.getDrawable() != null }
//    }
//
//    @Suppress("UNUSED_PARAMETER")
//    private fun render(context: DrawContext, renderTickCounter: RenderTickCounter) {
//        select()
//        val drawable = stickyCandidates[stickyIndex].getDrawable() ?: return
//        val size = drawable.preferredSize ?: IntSize.ZERO
//        width = size.width.toDouble()
//        height = size.height.toDouble()
//        val scaledWidth = context.scaledWindowWidth
//        val x = (scaledWidth - width - PADDING_X * 2) / 2
//        context.withPushMatrix {
//            matrices.translate(x, 5)
//            fill(
//                0, 0,
//                (width + PADDING_X * 2).toInt(), (height + PADDING_Y * 2).toInt(),
//                0x90000000.toInt()
//            )
//            matrices.translate(PADDING_X, PADDING_Y)
//            withScissor(IntSize(width.toInt() + PADDING_X, height.toInt()).atOrigin()) {
//                drawable.render(this, ScreenPosition(0, 0))
//            }
//        }
//    }
}

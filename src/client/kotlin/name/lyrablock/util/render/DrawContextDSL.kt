//package name.lyrablock.util.render
//
//import com.mojang.blaze3d.platform.GlStateManager
//import com.mojang.blaze3d.systems.RenderSystem
//import net.minecraft.client.gl.ShaderProgramKeys
//import net.minecraft.client.gui.DrawContext
//import net.minecraft.client.render.BufferRenderer
//import net.minecraft.client.render.Tessellator
//import net.minecraft.client.render.VertexFormat
//import net.minecraft.client.render.VertexFormats
//
//object DrawContextDSL {
//    fun DrawContext.drawLine(x1: Float, y1: Float, x2: Float, y2: Float) {
//        val matrix = this.matrices.peek().positionMatrix
//        val tessellator = Tessellator.getInstance()
//        val buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR)
//
//        buffer.vertex(matrix, x1, y1, 5f).color(0xFF_FFFFFF.toInt())
//        buffer.vertex(matrix, x2, y2, 5f).color(0xFF_FFFFFF.toInt())
//
////        RenderSystem.lineWidth(10f)
//        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR)
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
////        RenderSystem.disableDepthTest()
////        RenderSystem.disableBlend()
//        BufferRenderer.drawWithGlobalProgram(buffer.end())
////        RenderSystem.enableDepthTest()
////        RenderSystem.enableBlend()
//    }
//}

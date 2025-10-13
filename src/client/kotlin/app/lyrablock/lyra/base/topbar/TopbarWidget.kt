package app.lyrablock.lyra.base.topbar

import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.OrionWidget
import app.lyrablock.orion.components.Column
import app.lyrablock.orion.components.Container
import app.lyrablock.orion.components.Row
import app.lyrablock.orion.components.TextView
import app.lyrablock.orion.math.CrossAxisAlignment
import app.lyrablock.orion.math.EdgeInsets
import app.lyrablock.orion.math.OrionColor

class TopbarWidget : OrionWidget() {
    override fun build(): OrionComponent {
        return Container(
            color = OrionColor.BLACK.withAlpha(.5f).asGradient(),
            scissor = true,
            padding = EdgeInsets.all(5)
        ) {
            Row(gap = 15, crossAxisAlignment = CrossAxisAlignment.CENTER) {
                +TextView("Lorem ipsum dolor amit.")
                +Column {
                    +TextView("line1")
                    +TextView("line2").move(dy = 10)
                }
            }
        }
    }
}

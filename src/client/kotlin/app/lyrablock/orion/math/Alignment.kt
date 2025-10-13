package app.lyrablock.orion.math

/**
 * Horizontal alignment options for Row components
 */
enum class HorizontalAlignment {
    /** Align children to the left */
    LEFT,
    /** Center children horizontally */
    CENTER,
    /** Align children to the right */
    RIGHT
}

/**
 * Vertical alignment options for Column components
 */
enum class VerticalAlignment {
    /** Align children to the top */
    TOP,
    /** Center children vertically */
    CENTER,
    /** Align children to the bottom */
    BOTTOM
}

/**
 * Cross-axis alignment for Row components (vertical alignment of items in a horizontal layout)
 */
enum class CrossAxisAlignment {
    /** Align to the start of the cross axis */
    START,
    /** Center on the cross axis */
    CENTER,
    /** Align to the end of the cross axis */
    END,
    /** Stretch to fill the cross axis */
    STRETCH
}

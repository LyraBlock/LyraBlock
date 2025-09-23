package app.lyrablock.event

enum class CancellableEventResult(val isCancel: Boolean) {
    CANCEL(true),
    PASS(false)
}

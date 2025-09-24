package app.lyrablock.lyra.event

enum class CancellableEventResult(val isCancel: Boolean) {
    CANCEL(true),
    PASS(false)
}

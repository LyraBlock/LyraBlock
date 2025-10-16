package app.lyrablock.lyra.util.math

typealias TPair<T> = Pair<T, T>
typealias IntPair = TPair<Int>

@Deprecated("Use IntPoint or IntSize")
operator fun IntPair.plus(other: IntPair): IntPair {
    return IntPair(this.first + other.first, this.second + other.second)
}

@Deprecated("Use IntPoint or IntSize")
operator fun IntPair.unaryMinus(): IntPair {
    return -first to -second
}

@Deprecated("Use IntPoint or IntSize")
operator fun IntPair.minus(other: IntPair): IntPair {
    return this + -other
}


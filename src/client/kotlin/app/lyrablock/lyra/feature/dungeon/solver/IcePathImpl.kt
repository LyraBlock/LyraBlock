package app.lyrablock.lyra.feature.dungeon.solver

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Ice Path Solver using Iterative Deepening Depth-First Search (IDDFS).
 *
 * @author Gemini 2.5
 */
object IceFillPathfinder {
    @Serializable(MoveDirection.Serializer::class)
    enum class MoveDirection(val id: Byte, val delta: Pair<Int, Int>) {
        UP(1, -1 to 0), DOWN(2, 1 to 0), LEFT(3, 0 to -1), RIGHT(4, 0 to 1);

        object Serializer: KSerializer<MoveDirection> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor(MoveDirection::class.qualifiedName!!, PrimitiveKind.BYTE)

            override fun serialize(encoder: Encoder, value: MoveDirection) {
                encoder.encodeByte(value.id)
            }

            override fun deserialize(decoder: Decoder): MoveDirection {
                val code = decoder.decodeByte()
                return entries.first { it.id == code }
            }
        }
    }

    /**
     * Find the optimal Hamiltonian path with minimum turns
     *
     * @return List of coordinates representing the path, or null if no path exists
     */
    fun findOptimalHamiltonianPath(grid: Array<BooleanArray>): List<Pair<Int, Int>>? {
        val rows = grid.size
        if (rows == 0) return null
        val cols = grid[0].size
        if (cols % 2 == 0) return null

        val centerCol = cols / 2
        val start = rows - 1 to centerCol
        val end = 0 to centerCol

        // 1. Pre-calculation: Count total walkable cells
        var totalWalkableCount = 0
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (!grid[r][c]) totalWalkableCount++
            }
        }

        // Check if start/end points are blocked
        if (grid[start.first][start.second] || grid[end.first][end.second]) return null

        // IDDFS strategy: Iterate through the maximum allowed turns
        for (maxTurns in 0 until totalWalkableCount) {

            // Reset visited status and path for each iteration
            val visited = Array(rows) { BooleanArray(cols) { false } }
            val currentPath = mutableListOf<Pair<Int, Int>>()

            // Execute the bounded DFS search. lastMoveDirection is null at the start.
            val foundPath = boundedDfs(start, end, visited, currentPath, grid,
                totalWalkableCount, maxTurns, rows, cols, null)

            if (foundPath != null) {
                return foundPath // Found the optimal path
            }
        }

        return null // No solution found
    }

    /**
     * Depth-First Search with a turn count bound (Bounded DFS).
     * @param lastMoveDirection The direction used to reach the current cell. Used for turn calculation.
     */
    fun boundedDfs(current: Pair<Int, Int>, end: Pair<Int, Int>,
                   visited: Array<BooleanArray>,
                   path: MutableList<Pair<Int, Int>>,
                   grid: Array<BooleanArray>,
                   totalWalkableCount: Int,
                   maxTurns: Int,
                   rows: Int, cols: Int,
                   lastMoveDirection: MoveDirection?,
                   currentTurns: Int = 0): List<Pair<Int, Int>>? {

        path.add(current)
        visited[current.first][current.second] = true
        val currentVisitedCount = path.size

        // 1. Success condition: Reached the end AND covered all walkable cells
        if (current == end && currentVisitedCount == totalWalkableCount) {
            return path.toList()
        }

        // 2. Explore neighbors using the MoveDirection enum
        for (move in MoveDirection.entries) {
            val (dr, dc) = move.delta
            val nextR = current.first + dr
            val nextC = current.second + dc
            val next = nextR to nextC

            // Check boundaries, obstacles, and if already visited
            if (nextR in 0 until rows && nextC in 0 until cols &&
                !grid[nextR][nextC] && !visited[nextR][nextC]) {

                var nextTurns = currentTurns

                // Calculate turn change
                if (lastMoveDirection != null) {
                    // A turn occurs if the new move is different from the last move
                    if (move != lastMoveDirection) {
                        // Check for immediate reversal (e.g., UP then DOWN), which is prevented by the
                        // 'visited' check, but for completeness, we check direction change.
                        nextTurns++
                    }
                } else {
                    // This is the first move from the start, so we set the initial direction
                    // but don't count a turn.
                }

                // Pruning: Stop if the next step exceeds the maximum allowed turns
                if (nextTurns <= maxTurns) {
                    // Recursive call, passing the current 'move' as the new 'lastMoveDirection'
                    val result = boundedDfs(next, end, visited, path, grid,
                        totalWalkableCount, maxTurns, rows, cols, move, nextTurns)
                    if (result != null) {
                        return result // Propagate the result up
                    }
                }
            }
        }

        // 3. Backtrack: Reset visited status and remove current point from path
        visited[current.first][current.second] = false
        path.removeAt(path.size - 1)

        return null
    }
}

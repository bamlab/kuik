package com.theodo.apps.kuik.common.utils.file

fun findBlock(
    block: Regex,
    content: String,
): MatchResult? {
    val match = block.find(content)
    if (match != null) {
        val startIndex = match.range.first
        var braceCount = 1
        var currentIndex = match.range.last + 1

        while (braceCount > 0 && currentIndex < content.length) {
            when (content[currentIndex]) {
                '{' -> braceCount++
                '}' -> braceCount--
            }
            currentIndex++
        }

        if (braceCount == 0) {
            return MatchResult(startIndex, currentIndex - 1)
        }
    }
    return null
}

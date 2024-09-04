package io.github.estivensh4.kotlinmultiplatformwizard.common.utils.file

fun findDependencyInsertionPoint(kotlinBlockContent: String): Int? {
    // Search for 'commonMain.dependencies { }' within the kotlin block
    val commonMainMatch = findBlock(Regex("""commonMain\s*\.?\s*dependencies\s*\{\s*"""), kotlinBlockContent)
    if (commonMainMatch != null) {
        return commonMainMatch.endIndex
    }

    // Search for 'val commonMain by getting { }' pattern
    val valCommonMainMatch = findBlock(Regex("""val\s+commonMain\s+by\s+getting\s*\{\s*"""), kotlinBlockContent)
    if (valCommonMainMatch != null) {
        val valCommonMainMatchContent =
            kotlinBlockContent.substring(valCommonMainMatch.startIndex, valCommonMainMatch.endIndex + 1)
        val dependenciesMatch = findBlock(Regex("""dependencies\s*\{\s*"""), valCommonMainMatchContent)
        if (dependenciesMatch != null) {
            return valCommonMainMatch.startIndex + dependenciesMatch.endIndex
        }
    }

    return null
}

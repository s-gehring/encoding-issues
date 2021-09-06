package de.gehring.simon.encoding

data class AnalysisResult(
    val charsetUsedToSave: String,
    val charsetUsedToLoad: String,
    val codepoint: ByteArray,
    val interpretedResult: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnalysisResult

        if (charsetUsedToSave != other.charsetUsedToSave) return false
        if (charsetUsedToLoad != other.charsetUsedToLoad) return false
        if (!codepoint.contentEquals(other.codepoint)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = charsetUsedToSave.hashCode()
        result = 31 * result + charsetUsedToLoad.hashCode()
        result = 31 * result + codepoint.contentHashCode()
        return result
    }
}
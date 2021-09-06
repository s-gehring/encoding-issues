package de.gehring.simon.encoding

import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.*

@Throws(UnsupportedCharsetException::class)
fun `get string that was initially saved, assuming it was saved using another encoding than when loading`(
    readString: String,
    encodingUsedSaving: Charset,
    encodingUsedLoading: Charset
): AnalysisResult {
    try {
        val loadedBytes = readString.byteInputStream(encodingUsedLoading).readAllBytes()
        val resultString = String(loadedBytes, encodingUsedSaving)
        return AnalysisResult(
            charsetUsedToSave = encodingUsedSaving.displayName(),
            charsetUsedToLoad = encodingUsedLoading.displayName(),
            loadedBytes,
            resultString
        )
    } catch (e: Exception) {
        throw UnsupportedCharsetException("The combination of charsets is apparently not allowed.")
    }

}

val ENCODINGS: SortedMap<String, Charset> = Charset.availableCharsets()

fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("Input missing.")
    }

    val input = args[0]
    val expected = if (args.size > 1) args[1] else null

    val optimalFindings = ArrayList<AnalysisResult>()

    ENCODINGS.forEach {
        print("\t\t" + it)
    }
    println()


    ENCODINGS.forEach { usedReadingCharset ->
        print("\t" + usedReadingCharset.key)
        ENCODINGS.forEach { usedSavingCharset ->
            try {
                val result =
                    `get string that was initially saved, assuming it was saved using another encoding than when loading`(
                        input,
                        usedSavingCharset.value,
                        usedReadingCharset.value
                    )
                if (result.interpretedResult == expected) {
                    optimalFindings.add(
                        result
                    )
                }
                print("\t" + result)
            } catch (e: UnsupportedCharsetException) {
                print("\t<N/A>")
            }

        }
        println()
    }
    if (optimalFindings.isNotEmpty()) {
        println()
        println("Based on the expected result ($expected), the following scenarios are likely:")
        optimalFindings.forEach {
            println("\tThe string was saved with ${it.charsetUsedToSave} (as bytes 0x${it.codepoint.toHex()}), but wrongfully loaded with ${it.charsetUsedToLoad}.")
        }
    } else if (args.size > 1) {
        println("Failed to find any match. Your input must have been scrambled a few times.")
    }

}
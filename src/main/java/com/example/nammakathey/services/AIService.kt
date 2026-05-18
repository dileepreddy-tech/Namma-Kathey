package com.example.nammakathey.services

import java.net.URLEncoder
import java.util.Locale

data class StoryResult(
    val title: String,
    val subtitle: String,
    val story: String,
    val keyFacts: List<String>,
    val imageUrl: String,
)

object AIService {
    fun generateStory(theme: String): StoryResult {
        val encodedTheme = URLEncoder.encode(theme, "UTF-8")
        
        // AI Image generation as a fallback
        val aiImageUrl = "https://pollinations.ai/p/cinematic_portrait_of_Indian_freedom_fighter_${encodedTheme}?width=1080&height=1350&nologo=true"

        return when {
            theme.contains("Gandhi", ignoreCase = true) -> {
                StoryResult(
                    title = "Mahatma Gandhi",
                    subtitle = "Father of the Nation",
                    story = "Mahatma Gandhi led India to independence through non-violent civil disobedience. His life was a message of truth, peace, and sacrifice.",
                    keyFacts = listOf(
                        "Leader of the Salt Satyagraha.",
                        "Champion of non-violence (Ahimsa).",
                        "Inspired movements for civil rights worldwide.",
                        "Known as 'Bapu' (Father)."
                    ),
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7a/Mahatma-Gandhi%2C_studio%2C_1931.jpg"
                )
            }
            theme.contains("Bose", ignoreCase = true) || theme.contains("Subhash", ignoreCase = true) -> {
                StoryResult(
                    title = "Subhash Chandra Bose",
                    subtitle = "Netaji",
                    story = "Netaji Subhash Chandra Bose was a legendary revolutionary who founded the Azad Hind Fauj (Indian National Army) to fight for India's freedom.",
                    keyFacts = listOf(
                        "Gave the famous slogan 'Jai Hind'.",
                        "Founded the Indian National Army.",
                        "Twice elected as Congress President.",
                        "Called for 'Give me blood, and I shall give you freedom'."
                    ),
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/4c/Subhash_Chandra_Bose_NRB.jpg"
                )
            }
            theme.contains("Singh", ignoreCase = true) -> {
                StoryResult(
                    title = "Bhagat Singh",
                    subtitle = "Shaheed-e-Azam",
                    story = "Bhagat Singh was a fearless revolutionary whose bravery and sacrifice at a young age made him a folk hero and a symbol of resistance.",
                    keyFacts = listOf(
                        "Key figure in the HSRA.",
                        "Protested against the British Assembly.",
                        "Symbol of youth revolutionary spirit.",
                        "Remembered for 'Inquilab Zindabad'."
                    ),
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/5/54/Bhagat_Singh_1929.jpg"
                )
            }
            theme.contains("Ambedkar", ignoreCase = true) -> {
                StoryResult(
                    title = "Dr. B.R. Ambedkar",
                    subtitle = "Father of the Constitution",
                    story = "Dr. B.R. Ambedkar was a brilliant jurist and social reformer who dedicated his life to eradicating social inequality.",
                    keyFacts = listOf(
                        "Principal architect of the Constitution.",
                        "India's first Law Minister.",
                        "Champion of Dalit rights.",
                        "Awarded Bharat Ratna."
                    ),
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c3/Dr._Bhimrao_Ambedkar.jpg"
                )
            }
            theme.contains("Rani", ignoreCase = true) || theme.contains("Lakshmi", ignoreCase = true) || theme.contains("Jhansi", ignoreCase = true) -> {
                StoryResult(
                    title = "Rani Lakshmibai",
                    subtitle = "The Queen of Jhansi",
                    story = "Rani Lakshmibai was one of the leading figures of the 1857 Rebellion, known for her extraordinary bravery on the battlefield.",
                    keyFacts = listOf(
                        "The Warrior Queen of Jhansi.",
                        "Symbol of resistance against British rule.",
                        "Fought holding her son on her back.",
                        "Known as 'Jhansi ki Rani'."
                    ),
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/b/b2/Rani_Lakshmibai.png"
                )
            }
            else -> {
                val formattedTitle = theme.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                StoryResult(
                    title = formattedTitle,
                    subtitle = "National Hero",
                    story = "The hero $formattedTitle is an essential part of the tapestry of our nation's history, inspiring us with their courage.",
                    keyFacts = listOf(
                        "Dedicated their life to the nation.",
                        "Inspired thousands with their leadership.",
                        "A symbol of Indian patriotism."
                    ),
                    imageUrl = aiImageUrl
                )
            }
        }
    }
}

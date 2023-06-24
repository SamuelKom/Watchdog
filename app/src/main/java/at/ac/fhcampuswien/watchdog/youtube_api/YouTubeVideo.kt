package at.ac.fhcampuswien.watchdog.youtube_api

data class YouTubeVideo(
    val id: String,
    val snippet: Snippet,
    val status: Status
)
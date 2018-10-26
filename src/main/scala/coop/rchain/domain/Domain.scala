package coop.rchain.domain

sealed trait Domain

object TypeOfAsset {
  val t: Map[String, String] =
    Map("Stereo" -> "Stereo", "3D" -> "3D", "jpg" -> "jpg")
}

case class Interval[T](from: T, to: Option[T])

case class TemporalInterval(
    inMillis: Interval[Long],
    inUtc: Interval[String]
)

case class Metadata(k: String, v: String) extends Domain

case class Artwork(id: String, uri: String) extends Domain

case class Artist(
    id: String,
    title: String,
    name: String
) extends Domain

case class Audio(
    effect: String,
    uri: String,
    duration_ms: Long
) extends Domain

case class Song(
    id: String,
    title: String,
    name: String,
    audio: List[Audio],
    language: String
) extends Domain

case class Album(
    id: String,
    title: String,
    name: String,
    artworks: List[Artwork],
    duration_ms: Long,
    artists: List[Artist],
    uri: String
) extends Domain

case class SongMetadata(
    song: Song,
    artists: List[Artist],
    artwork: List[Artwork],
    album: Option[Album] = None
) extends Domain

object NameKey extends Enumeration {
  type NameKey = Value
  val newUserId, store, playCount, retrieveSong, retrieveMetadata, remunerate,
  play = Value
}

case class RSongJsonAsset(
    id: String,
    assetData: String,
    jsonData: String="""{"fakeData": "fakeValue"}"""
)
object ImmersionNames {
  val contractNames = Map(
    NameKey.newUserId -> """@["Immersion", "newUserId"]""",
    NameKey.store -> """@["Immersion", "store"]""",
    NameKey.playCount -> """@["Immersion", "playCount"]""",
    NameKey.retrieveSong -> """@["Immersion", "retrieveSong"]""",
    NameKey.retrieveMetadata -> """@["Immersion", "retrieveMetadata"]""",
    NameKey.remunerate -> """@["Immersion", "remunerate"]""",
    NameKey.play -> """@["Immersion", "play"]"""
  )
}

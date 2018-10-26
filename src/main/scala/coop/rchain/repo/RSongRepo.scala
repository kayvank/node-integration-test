package coop.rchain.repo

import com.typesafe.scalalogging.Logger
import coop.rchain.domain._
import scala.util._

object RSongRepo {
  val SONG_OUT = "SONG-OUT"
  val threshold = 1024
  val log = Logger("SongRepo")

  private def logDepth(s: String): String = {
    if (s.length <= threshold)
      s""""$s""""
    else {
      val mid = s.length / 2
      val l = logDepth(s.substring(0, mid))
      val r = logDepth(s.substring(mid))
      s"""(\n$l\n++\n$r\n)"""
    }
  }

  private def readFileAsByteArray(_size: Int) ={

    val str = Array.fill[Char](_size){'a'}.mkString
    log.info(s"size of arry.fill: ${str.size}")
    str
  }


  def asRholang(asset: RSongJsonAsset) = {
    log.info(s"name to retrieve song: ${asset.id}")
    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.jsonData}, "${asset.id}")"""
  }

  def asHexConcatRsong(_size: Int): Either[Err, String] = {
      Try( (readFileAsByteArray _
        andThen logDepth)(_size) ) match {
        case Success(v) =>
          log.info(s"logdebth: ${v.size}")
          Right(v)
        case Failure(e) =>
          log.error(s"${e.getMessage}")
          Left(Err(ErrorCode.unknown, e.getMessage, None))
      }
  }

}

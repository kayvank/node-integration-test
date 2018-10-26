package coop.rchain

import cats.effect._
import cats.syntax.all._
import com.typesafe.config.ConfigFactory
import coop.rchain.domain._
import coop.rchain.repo.RSongRepo._
import coop.rchain.repo.RholangProxy

import scala.util.Try


object Bootstrap extends IOApp {
  val cfg = ConfigFactory.load
  val appCfg = cfg.getConfig("coop.rchain.rsong")
  val (host,port) = ( appCfg.getString("grpc.host"),
    appCfg.getInt("grpc.ports.external"))
  val proxy = RholangProxy(host, port)

  def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case Some(a)  =>
        if ( (Try(a.toInt)).toOption.isDefined )
          IO(runTest(a.toInt) )
          .as(ExitCode.Success)
        else
          IO(fake(a)).as(ExitCode.Error)


      case None =>
        IO(runTest(4000)).as(ExitCode.Success)

    }
  def installContract(contractFile: String) = {
    val ret =
      for {
        _ <- proxy.deployFromFile(contractFile)
        propose <- proxy.proposeBlock
      } yield (propose)
    ret match {
      case Right(a) =>
        log.info(s"contract installed successfully with return code: ${a}")
      case Left(e) => log.error(s"contract failed to install with error: ${e}")
    }
  }

  def runTest(_size: Int) = {
    log.info(s"Start node integration test with payload size: ${_size}")
    val ret =
      for {
        _ <- proxy.deployFromFile("/rho/rsong.rho")
        _ <- proxy.proposeBlock
        _ <- loadAsset("song-1", _size)
        propose <- proxy.proposeBlock

      } yield (propose)
    ret match {
      case Right(a) =>
        log.info(s"assets are deployed and proposed. return code: ${a} ")
      case Left(e) => log.error(s"deploying assets failed.  Error code:  ${e}")
    }

  }

  def fake(a: String): Unit =
    log.error(s"$a is not a valid Int")

  def loadAsset(assetId: String, _size: Int): Either[Err, String] = {
    val ret = RSongJsonAsset(id = assetId,
      assetData =asHexConcatRsong(_size).toOption.get)
    log.info(s"Attempting to deploy/propose payload-size: ${ret.assetData.length}")

    (asRholang _ andThen proxy.deploy _)(ret)

  }

}

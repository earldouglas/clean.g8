package http

import cats.effect._
import com.comcast.ip4s._
import domain.entities.Count
import drivers.db.Transactor
import io.circe._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.ember.server._
import org.http4s.implicits._
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import usecases.Counter

import java.sql.Connection
import java.sql.DriverManager
import scala.util.chaining.scalaUtilChainingOps

object Main extends IOApp:

  given loggerFactory: LoggerFactory[IO] =
    Slf4jFactory.create[IO]

  def httpApp(counter: Counter[IO]): HttpApp[IO] =

    given countEncoder: Encoder[Count] =
      Encoder.instance: (count: Count) =>
        Json.fromInt(count.value)

    val routes =
      HttpRoutes.of[IO]:
        case GET -> Root / "count" =>
          for
            count <- counter.incrementAndGet
            response <- Ok(count.asJson)
          yield response

    routes.orNotFound

  def run(args: List[String]): IO[ExitCode] =

    given tx: Transactor[IO] with

      private lazy val c: Connection =
        Class.forName("org.h2.Driver")
        val connection: Connection =
          DriverManager.getConnection("jdbc:h2:mem:", "sa", "")
        connection.setAutoCommit(false)
        connection

      override def transact[A](k: Connection => IO[A]): IO[A] =
        k(c)
          .tap(_ => c.commit())
          .recoverWith:
            case e =>
              c.rollback()
              IO.raiseError(e)

    for
      _ <- drivers.db.init
      counter = drivers.db.counter[IO]
      exitCode <-
        EmberServerBuilder
          .default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(httpApp(counter))
          .build
          .use(_ => IO.never)
          .as(ExitCode.Success)
    yield exitCode

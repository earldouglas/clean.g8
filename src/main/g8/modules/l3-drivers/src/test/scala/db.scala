package drivers.db

import cats.effect.IO
import domain.entities.Count

import java.sql.Connection
import java.sql.DriverManager
import scala.util.chaining.scalaUtilChainingOps

class DbSuite extends munit.CatsEffectSuite:

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

  test("example test that succeeds"):

    for
      _ <- drivers.db.init
      svc = drivers.db.counter[IO]
      c1 <- svc.incrementAndGet
      c2 <- svc.incrementAndGet
      c3 <- svc.incrementAndGet
    yield
      assertEquals(c1, Count(1))
      assertEquals(c2, Count(2))
      assertEquals(c3, Count(3))

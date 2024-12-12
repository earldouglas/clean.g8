package drivers.mem

import cats.effect.IO
import domain.entities.Count

class MemSuite extends munit.CatsEffectSuite:

  test("example test that succeeds"):

    for
      svc <- drivers.mem.counter[IO]
      c1 <- svc.incrementAndGet
      c2 <- svc.incrementAndGet
      c3 <- svc.incrementAndGet
    yield
      assertEquals(c1, Count(1))
      assertEquals(c2, Count(2))
      assertEquals(c3, Count(3))

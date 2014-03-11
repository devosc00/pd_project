import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
import controllers._
import jp.t2v.lab.play2.auth.test.Helpers._
import java.io.File

class ApplicationSpec extends Specification {

  object config extends AuthConfigImpl

  "Messages" should {
    "return list when user is authorized" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase(name = "default", options = Map("DB_CLOSE_DELAY" -> "-1")))) {
      val res = Users.create(FakeRequest().withLoggedIn(config)(1))
      contentType(res) must beSome("text/html")
    }
  }

}
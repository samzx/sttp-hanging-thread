import cats.effect.{ExitCode, Resource}
import monix.eval.{Task, TaskApp}
import sttp.client.asynchttpclient.monix.AsyncHttpClientMonixBackend
import sttp.client.{quickRequest, _}

object Main extends TaskApp {
  def run(args: List[String]): Task[ExitCode] = {
    Resource
      .make(AsyncHttpClientMonixBackend())(backend => Task(backend.close()))
      .use { implicit backend =>
        val request = quickRequest.get(uri"https://postman-echo.com/get?foo=bar")
        request.send().map { response =>
          println(response.body)
          ExitCode.Success
        }
      }
  }
}

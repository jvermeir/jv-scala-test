import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{Http, Response}
import com.twitter.finagle.Service
import com.twitter.util.Future
import java.net.InetSocketAddress
import util.Properties

object Web {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    println("Starting on port:"+port)
    ServerBuilder()
      .codec(Http())
      .name("hello-server")
      .bindTo(new InetSocketAddress(port))
      .build(new Hello)
  }
}



class Hello extends Service[HttpRequest, HttpResponse] {
    val PAGE = """
    <!doctype html>
    <html ng-app>
      <head>
        <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.0.3/angular.min.js"></script>
      </head>
      <body>
        <div>
          <label>Name:</label>
          <input type="text" ng-model="yourName" placeholder="Enter a name here">
          <hr>
          <h1>Hello {{yourName}}!</h1>
        </div>
      </body>
    </html>
    """
  def apply(req: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    response.setStatusCode(200)
    response.setContentString(PAGE)
    Future(response)
  }
}

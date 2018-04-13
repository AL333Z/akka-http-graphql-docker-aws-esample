import java.util.concurrent.TimeUnit

import org.mongodb.scala.connection.{ClusterSettings, SocketSettings}
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCredential, ServerAddress}

import scala.collection.JavaConverters._
import scala.language.postfixOps

object Mongo {

  case class MongoConfig(
                          authrequired: Boolean,
                          username: String,
                          password: String,
                          servers: Seq[String],
                          serverPort: Int,
                          socketReadTimeoutSeconds: Int
                        )

  def fromConfig(config: MongoConfig): MongoClient = {

    val builder = MongoClientSettings.builder()
      .socketSettings(
        SocketSettings.builder()
          .readTimeout(config.socketReadTimeoutSeconds, TimeUnit.SECONDS)
          .build()
      )
      .clusterSettings(
        ClusterSettings.builder()
          .hosts(config.servers.map(addr => ServerAddress(host = addr, port = config.serverPort)).asJava)
          .build()
      )

    MongoClient(
      if (config.authrequired)
        builder.credential(
          MongoCredential.createScramSha1Credential(
            userName = config.username,
            password = config.password.toCharArray,
            source = "admin"
          )
        ).build()
      else builder.build()
    )
  }

}

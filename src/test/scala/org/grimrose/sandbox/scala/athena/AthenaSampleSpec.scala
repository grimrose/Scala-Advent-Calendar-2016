package org.grimrose.sandbox.scala.athena

import java.sql.ResultSet

import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
import org.scalatest._
import scalikejdbc._

class AthenaSampleSpec
    extends FlatSpec
    with DiagrammedAssertions
    with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  behavior of "Athena"

  override protected def beforeAll(): Unit = {
    // TODO replace your bucket name
    val stagingDir = "s3://my-athena-result-bucket/test/"

    val config = new HikariConfig()
    config.setJdbcUrl("jdbc:awsathena://athena.us-east-1.amazonaws.com:443/")
    config.addDataSourceProperty("aws_credentials_provider_class", "com.amazonaws.auth.PropertiesFileCredentialsProvider")
    config.addDataSourceProperty("aws_credentials_provider_arguments", "./conf/.athenaCredentials")
    config.addDataSourceProperty("s3_staging_dir", stagingDir)
    config.addDataSourceProperty("log_path", "./logs/athenajdbc.log")

    // experimental
    config.setMaximumPoolSize(1)

    // Disabling auto-commit mode not supported
    config.setAutoCommit(true)

    val dataSource = new HikariDataSource(config)
    ConnectionPool.singleton(new DataSourceConnectionPool(dataSource))
  }

  it should "be found tables" in {
    using(ConnectionPool.borrow()) { conn ⇒
      using(conn.createStatement()) { stmt ⇒
        // language=SQL
        val sql =
          """
            SELECT 
              os, 
              COUNT(*) AS count 
            FROM mydatabase.cloudfront_logs 
            WHERE date BETWEEN date '2014-07-05' AND date '2014-08-05' 
            GROUP BY os
          """.stripMargin
        val rs = stmt.executeQuery(sql)

        val result = resultSetToSeq(rs) { rs ⇒
          val os = rs.getString("os")
          val count = rs.getInt("count")
          (os, count)
        }
        result.foreach(println)
      }
    }
  }

  it should "be count by status" in {
    val list = using(ConnectionPool.borrow()) { conn ⇒
      using(conn.createStatement()) { stmt ⇒
        // language=SQL
        val sql =
          """
            SELECT
              status
            , COUNT(*) AS count_by_status
            FROM mydatabase.cloudfront_logs
            GROUP BY status
            ORDER BY status
          """.stripMargin
        val rs = stmt.executeQuery(sql)

        resultSetToSeq(rs) { rs ⇒
          val code = rs.getString("status")
          val count = rs.getInt("count_by_status")
          (code, count)
        }
      }
    }

    val total = list.foldLeft(0) { (acc, t) ⇒ acc + t._2 }

    list
      .map { case (code, count) ⇒ (code, count, count.toDouble / total * 100) }
      .foreach {
        case (code, count, rate) ⇒
          println(s"code:$code\tcount:$count\trate:$rate")
      }
  }

  private def resultSetToSeq[A](rs: ResultSet)(fn: ResultSet ⇒ A): Seq[A] = {
    Iterator.continually(rs).takeWhile(_.next()).map(fn).toSeq
  }
}

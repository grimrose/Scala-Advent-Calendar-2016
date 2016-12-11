# Scala-Advent-Calendar-2016

Scala Advent Calendar 2016 http://qiita.com/advent-calendar/2016/scala

## requirement

Setting Up Amazon Athena

http://docs.aws.amazon.com/ja_jp/athena/latest/ug/setting-up.html

Getting Started

http://docs.aws.amazon.com/ja_jp/athena/latest/ug/getting-started.html

## setting

1. Download Athena JDBC Driver

    ```sh
    aws s3 cp s3://athena-downloads/drivers/AthenaJDBC41-1.0.0.jar ./libs/
    ```

1. Create athenaCredentials file at `conf/.athenaCredentials`

    @see [Using a Credentials Provider](http://docs.aws.amazon.com/ja_jp/athena/latest/ug/connect-with-jdbc.html#using-a-credentials-provider)

    ```
    accessKey = ACCESSKEY
    secretKey = SECRETKEY
    ```

1. Replace bucket name

    ```scala
    override protected def beforeAll(): Unit = {
        // TODO replace your bucket name
        val stagingDir = "s3://my-athena-result-bucket/test/"

    }
    ```

## run

```sh
sbt test
```

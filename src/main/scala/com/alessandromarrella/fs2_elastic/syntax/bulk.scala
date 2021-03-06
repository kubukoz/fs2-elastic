package com.alessandromarrella.fs2_elastic.syntax

import cats.effect.Async
import fs2.Stream
import org.elasticsearch.action.bulk.{BulkItemResponse, BulkRequest, BulkResponse}
import org.elasticsearch.client.RestHighLevelClient
import com.alessandromarrella.fs2_elastic.io
import org.apache.http.Header

private[syntax] trait bulk {
  implicit class ElasticClientBulkOps[F[_]](
      val client: Stream[F, RestHighLevelClient]) {
    def bulk(bulkRequest: BulkRequest, headers: Header*): Stream[F, BulkResponse] =
      client.through(io.bulk.bulk(bulkRequest, headers:_*))
  }
  implicit class BulkOps[F[_]](
      val bulkResponseStream: Stream[F, BulkResponse]) {
    def stream(implicit F: Async[F]): Stream[F, BulkItemResponse] =
      streamFromJavaIterable(bulkResponseStream)
  }

}

object bulk extends bulk

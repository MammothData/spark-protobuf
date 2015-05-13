package com.mammothdatallc.spark

import com.mammothdatallc.proto.MyProtocolBuffers
import com.twitter.elephantbird.mapreduce.input.MultiInputFormat
import com.twitter.elephantbird.mapreduce.io.BinaryWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.mapreduce.Job
import org.apache.spark._

object SparkReadProtoBuf {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("SparkReadProtoBuf")
    val sc = new SparkContext(sparkConf)

    val conf = Job.getInstance().getConfiguration
    conf.set("io.compression.codecs", "com.hadoop.compression.lzo.LzopCodec")
    conf.set("io.compression.codec.lzo.class", "com.hadoop.compression.lzo.LzoCodec")

    MultiInputFormat.setClassConf(classOf[MyProtocolBuffers.MyProtocolBuffer], conf)

    val data = sc
      .newAPIHadoopFile[LongWritable, BinaryWritable[MyProtocolBuffers.MyProtocolBuffer],
      MultiInputFormat[MyProtocolBuffers.MyProtocolBuffer]](
        "/tmp/SparkWriteProtoBuf/*.lzo", classOf[MultiInputFormat[MyProtocolBuffers.MyProtocolBuffer]],
        classOf[LongWritable],
        classOf[BinaryWritable[MyProtocolBuffers.MyProtocolBuffer]], conf)

    val input = data.map { case (x, y) =>
      (x.get, y match {
        case b: BinaryWritable[MyProtocolBuffers.MyProtocolBuffer] => b.get()
        case _ => throw new Exception("Unexpected input")
      }
        )
    }

    println(input.collect().mkString(","))

    sc.stop()

  }

}